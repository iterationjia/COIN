<template>
  <div>
    <el-row>
      <el-col :span="4.9" v-for="(o, index) in divideGraphList.length" :key="o"
              :offset="index > 0 ? divideGraphList.length : 0" class="col">
        <el-card :body-style="{ padding: '0px' }" class="cards">
          <div :id="idSelect(index)"></div>
          <el-image
            class="image"
            :src="divideGraphList[index].url"
            @dblclick="jumpToGraph(index)">
          </el-image>
          <div style="padding: 8px;" @dblclick="jumpToGraph(index)">
            <span>{{divideGraphList[index].name}}</span>
            <div class="bottom clearFix" @click="setCurrentGraph(index)">
              <time class="time">{{ divideGraphList[index].time }}</time>
              <el-dropdown @command="handleCommand" trigger="click">
                <span class="el-dropdown-link">图谱管理<i class="el-icon-arrow-down el-icon--right"></i></span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="rename">重命名</el-dropdown-item>
                  <el-dropdown-item command="addStarTag" :disabled="divideGraphList[index].favored">设为星标
                  </el-dropdown-item>
                  <el-dropdown-item command="removeStarTag" :disabled="!divideGraphList[index].favored">取消星标
                  </el-dropdown-item>
                  <!--                <el-dropdown-item disabled>分享</el-dropdown-item>-->
                  <el-dropdown-item divided command="delete">删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>
        </el-card>
        <el-dialog :visible.sync="editGraphVisible" width="30%">
          <el-input v-model="input_chart" placeholder="请输入图谱名"></el-input>
          <span slot="footer" class="dialog-footer">
      <el-button @click="editGraphVisible = false">取 消</el-button>
      <el-button type="primary" @click="handleEditGraphClose">确 定</el-button>
      </span>
        </el-dialog>
      </el-col>
    </el-row>

    <div class="block">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page.sync="currentPage"
        :page-size=this.divideNumber
        layout="prev, pager, next, jumper"
        :total="this.userGraphList.length">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import {mapActions, mapMutations, mapGetters} from 'vuex'

  export default {
    name: "GraphCard",
    async mounted() {
      await this.getCurrentUserGraphList()

      window.onresize = () => {
        this.cardDivide()
      }
      this.cardDivide()
    },
    data() {
      return {
        //翻页
        currentPage: 1,

        //搜索
        bolSearch: false,

        currentGraph: 0,
        //userGraphList 为后端图谱列表数据， divideGraphList请勿修改

        divideGraphList: [],

        temp_graph: [],

        divideNumber: 0,

        input_chart: '',

        editGraphVisible: false,

      }
    },
    computed: {
      ...mapGetters([
        'currentGraphId',
        'userGraphList',
        'customGraphParams'
      ])
    },
    methods: {
      ...mapMutations([
        'set_currentGraphId',
        'set_customGraphParams',
        'set_userParams',
      ]),

      ...mapActions([
        'getUserGraphList',
        'deleteGraph',
        'editGraph',
      ]),

      async getCurrentUserGraphList() {
        await this.set_userParams({
          id: localStorage.getItem('userId'),
          name: localStorage.getItem('userName'),
        })
        await this.getUserGraphList();
        console.log(this.userGraphList)
      },

      async handleCommand(index) {
        switch (index) {
          case 'delete':
            await this.deleteGraph();
            this.$parent.refreshGraph();
            break;
          case 'addStarTag':
            this.set_customGraphParams({
              isFavored: true,
              favored: true
            });
            await this.editGraph();
            this.$parent.refreshGraph();
            break;
          case 'removeStarTag':
            this.set_customGraphParams({
              isFavored: false,
              favored: false
            });
            await this.editGraph();
            this.$parent.refreshGraph();
            break;
          case 'rename':
            this.editGraphVisible = true
            break;
          default:
            break;
        }
      },

      async handleEditGraphClose() {
        this.set_customGraphParams({
          name: this.input_chart,
        })
        await this.editGraph()
        this.$parent.refreshGraph()
        this.input_chart = ''
      },

      setCurrentGraph(index) {
        this.set_currentGraphId(this.userGraphList[index].id)
      },
      jumpToGraph(index) {
        this.set_currentGraphId(this.userGraphList[index].id)
        this.set_customGraphParams(this.divideGraphList[index])
        localStorage.setItem('graphId', this.currentGraphId)
        this.$router.push('/graphs/' + this.currentGraphId)
      },
      idSelect(index) {
        if (this.divideGraphList[index].favored) {
          return "marker"
        } else {
          return "unMarker"
        }
      },
      cardDivide() {
        let windowWidth = window.innerWidth;
        this.divideNumber = parseInt((windowWidth - 120) / 310) * 2

        this.divideGraphList = [];
        for (let i = 0; i < this.divideNumber; i++) {
          if (i < this.userGraphList.length) {
            this.divideGraphList.push(this.userGraphList[i])
          }
        }
      },

      handleCurrentChange(val) {
        val = val - 1;
        this.divideGraphList = [];
        for (let i = val * this.divideNumber; i < val * this.divideNumber + this.divideNumber; i++) {
          if (i < this.userGraphList.length) {
            this.divideGraphList.push(this.userGraphList[i])
          }
        }
      },
      search(val) {
        this.bolSearch = true;
        this.divideGraphList = [];
        for (let i = 0; i < this.userGraphList.length; i++) {
          let temp = 'false';
          for (let j = 0; j < val.length; j++) {
            if (val.length > this.userGraphList[i].name.length) {
              temp = 'true';
              break;
            }
            if (val[j] !== this.userGraphList[i].name[j]) {
              temp = 'true';
              break;
            }
          }
          if (temp === 'false') {
            this.divideGraphList.push(this.userGraphList[i])
          }
        }
        this.temp_graph = this.userGraphList;
        this.userGraphList = this.divideGraphList;
      },

      resetGraphList() {
        if (this.bolSearch === true) {
          this.bolSearch = false;
          this.userGraphList = this.temp_graph;
          this.divideGraphList = this.userGraphList;
          this.handleCurrentChange(1)
        }
      },

      resetbolSearch() {
        this.bolSearch = false;
      }
    },
  }
</script>

<style scoped>

  .cards {
    margin-top: 18px;
    margin-left: 14px;
    height: 270px;
    border-radius: 6px;
  }

  .cards:hover {
    opacity: 0.8;
    border-bottom: 1px solid #000;
  }

  .col {
    margin-left: 48px;
    margin-bottom: 25px
  }

  .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    border-radius: 8px 8px 3px 3px;
    height: 200px;
    width: 230px;
    display: block;
  }

  .clearFix:before,
  .clearFix:after {
    display: table;
    content: "";
  }

  .clearFix:after {
    clear: both
  }

  .el-dropdown-link {
    margin-left: 20px;
    cursor: pointer;
    color: #409EFF;
  }

  .el-icon-arrow-down {
    font-size: 12px;
  }

  #backColor {
    background-color: #54a0ff;
  }

  #marker:before {
    position: absolute;
    margin-left: -125px;
    margin-top: 20px;
    padding-right: 10px;
    z-index: 1;
    font-weight: bold;
    line-height: 0;
    color: #eaeaea;
    border: 15px solid #54a0ff;
    border-right-color: transparent;
    content: '❤';
    box-shadow: 0 5px 5px -5px #000;
  }

  #marker:after {
    content: '';
    position: absolute;
    margin-left: -125px;
    margin-top: 50px;
    border: 4px solid #55a0ff;
    border-left-color: transparent;
    border-bottom-color: transparent;
  }
</style>
