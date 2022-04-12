<template>
  <div class="box">
    <div class="header">
      <img class="icon" src="https://lililizi.oss-cn-beijing.aliyuncs.com/%E7%8C%AB%E5%92%AA%20%281%29.png" height="25"
           width="25"/>
      <p class="title"><strong>ACoin</strong></p>

      <el-select v-model="modelSelect" class="selectModel" @change="update">
        <el-option v-for="item in modelOptions"
                   :key="item.value"
                   :label="item.label"
                   :value="item.label"></el-option>
      </el-select>

      <div class="option-button-area" v-show="fullWidth">
        <el-popover
          placement="bottom-start"
          trigger="hover"
          content="保存退出"
          @click.native="SaveAndQuit"
          :open-delay=1000>
          <el-button slot="reference" icon="el-icon-back" class="option-button" size="small"></el-button>
        </el-popover>

        <el-popover
          placement="bottom-start"
          trigger="hover"
          @click.native="graphUpdate"
          content="上传云端"
          :open-delay=1000>
          <el-button slot="reference" icon="el-icon-upload" class="option-button" size="small"></el-button>
        </el-popover>

        <el-popover
          placement="bottom-start"
          trigger="hover"
          @click.native="reset"
          content="图谱复位"
          :open-delay=1000>
          <el-button slot="reference" icon="el-icon-refresh-right" class="option-button" size="small"></el-button>
        </el-popover>

        <el-popover
          placement="bottom-start"
          trigger="hover"
          content="本地保存"
          @click.native="StoreTableVisible = true"
          :open-delay=1000>
          <el-button slot="reference" icon="el-icon-download" class="option-button" size="small"></el-button>
        </el-popover>
        <el-dialog
          :visible.sync="StoreTableVisible"
          width="20%">
               <span>
                 <el-button @click="StoreTableToPage">保存图片</el-button>
               </span>
          <span style="margin-left: 20px">
                 <el-button @click="StoreTableToData">导出数据</el-button>
               </span>
        </el-dialog>

        <el-button slot="reference" icon="el-icon-search" class="option-button" size="small"
                   @click="set_searchBoardVisible(true)"></el-button>

        <el-popover
          placement="bottom-start"
          trigger="hover"
          content="数值统计"
          @click.native="set_numericalVisible(true)"
          :open-delay=1000>
          <el-button slot="reference" icon="el-icon-data-analysis" class="option-button" size="small"></el-button>
        </el-popover>

        <el-popover
          placement="bottom-start"
          trigger="hover"
          content="清空"
          @click.native="handleDeleteAll"
          :open-delay=1000>
          <el-button slot="reference" icon="el-icon-delete" class="option-button" size="small"></el-button>
        </el-popover>
      </div>

      <Avatar></Avatar>

      <div class="pop-button" v-show="!fullWidth">
        <el-dropdown>
          <el-button icon="el-icon-s-operation"></el-button>
          <el-dropdown-menu slot="dropdown" style="width: 110px">
            <el-button @click.native="SaveAndQuit" class="option-button2"> 保存退出</el-button>
            <el-button @click.native="graphUpdate" class="option-button2">上传云端</el-button>
            <el-button @click.native="reset" class="option-button2">图谱复位</el-button>

            <el-popover
              placement="right"
              trigger="click"
              :open-delay=100>
              <el-button slot="reference" class="option-button2">本地保存</el-button>
              <div>
                <el-button @click="StoreTableToPage">保存图片</el-button>
                <el-button @click="StoreTableToData">导出数据</el-button>
              </div>
            </el-popover>


            <el-button command="search" class="option-button2" @click.native="set_searchBoardVisible(true)">搜索过滤
            </el-button>

            <el-button command="number" class="option-button2" @click.native="set_numericalVisible(true)">数值统计
            </el-button>

            <el-button command="delete" class="option-button2" @click.native="handleDeleteAll">清空</el-button>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <div class="body" @contextmenu.prevent="openMenu($event)">
      <!-- 添加对象 -->
      <el-dialog
        title="添加对象"
        :visible.sync="AddObjectVisible"
        width="30%">
            <span>
              <el-input v-model="input_Object" placeholder="对象名"></el-input>
            </span>
        <span>
                 <el-select v-model="ShapeValue" placeholder="形状" style="width: 48%;float: left;margin-top: 10px">
                <el-option
                  v-for="item in ShapeOptions"
                  :key="item.ShapeValue"
                  :label="item.ShapeLabel"
                  :value="item.ShapeValue">
                </el-option>
              </el-select>
              <el-input v-model="input_NodeSize" placeholder="节点大小(外接圆半径)"
                        style="width: 48%;float: left;margin-top: 10px;margin-left: 4%;"></el-input>
            </span>

        <span>
                 <div class="ColorBlock" style="width: 48%;float: left;margin-top: 10px">
                   <p style="float: left;width: 35%;margin-left: 10%;margin-bottom: 10px">节点颜色:</p>
                  <el-color-picker v-model="input_NodeColor"
                                   style="float: left;margin-left: 10%;margin-bottom: 10px"></el-color-picker>
                  </div>
              <el-input v-model="input_FontSize" placeholder="字体大小"
                        style="width: 48%;float: left;margin-left: 4%;margin-top: 10px;margin-bottom: 10px"></el-input>
            </span>
        <span slot="footer" class="dialog-footer">
            <el-button @click="AddObjectVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleAddObjectClose">确 定</el-button>
            </span>
      </el-dialog>
      <!--      <DragElement></DragElement>-->
      <ul v-show="formVisible && modelSelect==='力导图模式'" :style="{left:left+'px',top:top+'px'}" class="contextmenu">
        <li @click="AddObjectVisible=true">增加节点</li>
        <li @click="beforeAddRelation">增加关系</li>
      </ul>
      <div v-show="modelSelect==='力导图模式'">
        <ForceMap id="pic" ref="ForceMap"></ForceMap>
      </div>
      <div v-show="modelSelect==='排版模式'">
        <TypeSetting v-if="modelUpdate"></TypeSetting>
      </div>

    </div>
  </div>
</template>

<script>
  import FloatingWindow from './Components/floatingWindow'
  import ForceMap from './Models/ForceMap'
  import TypeSetting from './Models/TypeSetting'
  import html2canvas from "html2canvas";
  import FileSaver from "file-saver";
  import Avatar from "./Components/Avatar";
  import {mapGetters, mapMutations, mapActions} from 'vuex'

  export default {
    name: "id",
    watch: {
      formVisible(value) {
        if (value) {
          document.body.addEventListener('click', this.closeMenu)
        } else {
          document.body.removeEventListener('click', this.closeMenu)
        }
      }
    },
    mounted() {
      this.SearchDataSer();
      window.onresize = () => {
        this.page_width();
      }
      this.page_width();
    },
    components: {
      Avatar,
      FloatingWindow,
      ForceMap,
      TypeSetting,
    },
    computed: {
      ...mapGetters([
        'customNodeParams',
        'currentGraphId',
        'customGraphRes',
        'customGraphParams',
      ]),
    },
    data() {
      return {
        modelUpdate: false,
        // 添加节点
        AddObjectVisible: false,
        input_Object: '',
        ShapeOptions: [{
          ShapeValue: 'circle',
          ShapeLabel: '圆形'
        }, {
          ShapeValue: 'rect',
          ShapeLabel: '方形'
        }, {
          ShapeValue: 'diamond',
          ShapeLabel: '菱形'
        }, {
          ShapeValue: 'triangle_up',
          ShapeLabel: '三角形'
        }],
        ShapeValue: '',
        input_NodeSize: '',
        input_NodeColor: '',
        input_FontSize: '',

        userName: localStorage.getItem('userName'),
        searchInput: '',
        clearSearch: '',
        formVisible: false,
        top: 0,
        left: 0,
        modelSelect: '力导图模式',
        modelOptions: [{
          value: '1', label: '力导图模式'
        }, {
          value: '2', label: '排版模式'
        }],
        fullWidth: true,
        dataUrl: '',
        timer: '',

        //搜索
        nodes: {},
        links: {},
        rectNodes: {},
        diamondNodes: {},
        triangleUpNodes: {},
        nodesName: {},
        linksName: {},

        StoreTableVisible: false,
      }
    },
    methods: {
      ...mapMutations([
        'set_customNodeParamsClear',
        'set_nodeClickVisible',
        'set_relationClickVisible',
        'set_searchBoardVisible',
        'set_customNodeParams',
        'set_customGraphParams',
        'set_numericalVisible',
      ]),
      ...mapActions([
        'addCustomNode',
        'getCustomGraph',
        'deleteAllNodesInGraph',
        'editCustomNode',
        'editGraph',
      ]),
      openMenu(e) {
        const menuMinWidth = 105
        const offsetLeft = this.$el.getBoundingClientRect().left // container margin left
        const offsetWidth = this.$el.offsetWidth // container width
        const maxLeft = offsetWidth - menuMinWidth // left boundary
        const left = e.clientX - offsetLeft // 15: margin right

        if (left > maxLeft) {
          this.left = maxLeft
        } else {
          this.left = left
        }

        this.top = e.clientY// fix 位置bug
        this.formVisible = true
      },
      closeMenu() {
        this.formVisible = false
      },
      page_width() {//获取屏幕宽度
        let screenWidth = window.innerWidth;
        this.fullWidth = screenWidth >= 940;
      },
      reset() {
        this.$refs.ForceMap.reset()
      },
      //保存缩略图＋退回主页
      async SaveAndQuit() {
        this.set_nodeClickVisible(false)
        this.set_relationClickVisible(false)
        this.set_searchBoardVisible(false)
        this.set_numericalVisible(false)
        this.modelSelect = '力导图模式'
        //先将图谱缩小
        this.$refs.ForceMap.SaveAndQuit_ZoomIn()
        // 将本地图谱上传
        await this.graphUpdate()
        //后保存
        await html2canvas(document.getElementById('pic'), {
          backgroundColor: 'white',
          useCORS: true, //支持图片跨域
          scale: 1, //设置放大的倍数
        })
          .then((canvas) => {
            this.dataUrl = canvas.toDataURL('image/png');
          });
        this.set_customGraphParams({
          id: parseInt(localStorage.getItem('graphId')),
          url: this.dataUrl,
          time: new Date().toLocaleDateString()
        })
        await this.editGraph()
        await this.$router.push('/home')
      },
      SearchDataSer() {
        this.nodes = this.$refs.ForceMap.nodes;
        this.links = this.$refs.ForceMap.links;
        this.rectNodes = this.$refs.ForceMap.rectNodes;
        this.diamondNodes = this.$refs.ForceMap.diamondNodes;
        this.triangleUpNodes = this.$refs.ForceMap.triangleUpNodes;
        this.nodesName = this.$refs.ForceMap.nodesName;
        this.linksName = this.$refs.ForceMap.linksName;
      },
      //保存图谱图片
      StoreTableToPage() {
        html2canvas(document.getElementById('pic'), {
          backgroundColor: 'white',
          useCORS: true, //支持图片跨域
          scale: 1, //设置放大的倍数
        })
          .then((canvas) => {
            // 生成图片导出
            const a = document.createElement('a');
            a.href = canvas.toDataURL('image/png');
            a.download = this.$refs.ForceMap.customGraphParams.name;
            a.click();
          });

        this.StoreTableVisible = false;
      },
      //保存图谱数据
      StoreTableToData() {
        const data = JSON.stringify(this.customGraphRes.nodes);
        const blob = new Blob([data], {type: ''});
        FileSaver.saveAs(blob, this.$refs.ForceMap.customGraphParams.name + "_nodes")

        const data1 = JSON.stringify(this.customGraphRes.links);
        const blob1 = new Blob([data1], {type: ''});
        FileSaver.saveAs(blob1, this.$refs.ForceMap.customGraphParams.name + "_links")
      },
      // 添加对象
      async handleAddObjectClose() {
        const data = {
          graphId: this.currentGraphId,
          name: this.input_Object,
          x: 0,
          y: 0,
          typesetting_x: 0,
          typesetting_y: 0,
          shape: this.ShapeValue,
          color: this.input_NodeColor,
          node_size: this.input_NodeSize,
          font_size: this.input_FontSize,
          isShown: true,
          isHighlighted: false,
        }

        await this.set_customNodeParamsClear(data)
        await this.addCustomNode()

        await this.getCustomGraph()
        await this.$refs.ForceMap.removeSvg()
        await this.$refs.ForceMap.initGraph(this.customGraphRes)

        // 关闭视图
        this.AddObjectVisible = false

        //清空
        this.input_Object = '';
        this.ShapeValue = '';
        this.input_NodeSize = '';
        this.input_NodeColor = '';
        this.input_FontSize = '';
        this.$refs.ForceMap.numericalUpdate()
      },
      // 添加关系预备方法
      beforeAddRelation() {
        this.$refs.ForceMap.flagAddRelation = true
        this.$message.success('使用鼠标拖拽节点进行关系添加')
      },
      // 清空当前图谱
      async handleDeleteAll() {
        await this.deleteAllNodesInGraph()
        await this.getCustomGraph()
        await this.$refs.ForceMap.removeSvg()
        await this.$refs.ForceMap.initGraph(this.customGraphRes)
        await this.$refs.ForceMap.numericalUpdate()
      },
      // 图谱上传
      async graphUpdate() {
        for (let i = 0; i < this.customGraphRes.nodes.length; i++) {
          this.set_customNodeParams(this.customGraphRes.nodes[i])
          await this.editCustomNode();
        }
        this.$message.success('图谱上传成功')
      },

      update(){
        if(this.modelSelect==='排版模式'){
          this.$refs.ForceMap.removeSvg()
        }else{
          this.$refs.ForceMap.initGraph(this.customGraphRes)
        }
        this.modelUpdate = false;
        this.$nextTick(() => {
          this.modelUpdate = true
        })
      },
    }
  }
</script>

<style scoped>
  * {
    font-family: "Roboto", Arial, sans-serif;
  }

  .header {
    height: 68px;
  }

  .icon {
    float: left;
    margin-top: 19px;
    margin-left: 32px;
  }

  .title {
    float: left;
    font-size: 20px;
    margin-left: 10px;
  }

  .selectModel {
    float: left;
    margin-top: 10px;
    margin-left: 20px;
    margin-right: 20px;
  }

  .option-button {
    font-size: 20px;
    float: left;
    margin-top: 10px;
    margin-left: 10px;
  }

  .option-button2 {
    background-repeat: no-repeat;
    background-color: transparent;
    border: 0 none;
    margin-left: 10px
  }

  .pop-button {
    float: right;
    margin-top: 10px;
    margin-right: 10px;
  }


  .body {
    height: 650px;
    width: 90%;
    margin-left: 5%;
    margin-right: 5%;
    border-radius: 10px;
    background-color: white;
  }

  .contextmenu {
    margin: 0;
    background: #fff;
    z-index: 3000;
    position: absolute;
    list-style-type: none;
    padding: 5px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);
  }

  li {
    margin: 0;
    padding: 7px 16px;
    cursor: pointer;
  }

</style>
