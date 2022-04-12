<template>
  <div class="box">
    <div class="header">
      <img class="icon" src="https://lililizi.oss-cn-beijing.aliyuncs.com/%E7%8C%AB%E5%92%AA%20%281%29.png" height="25"
           width="25"/>
      <p class="title"><strong>ACoin</strong></p>
      <el-input
        v-show="this.Modal==='Warehouse'"
        class="inline-input"
        v-model="searchInput"
        placeholder="Search"
        clearable
        @clear="clearSearch1">
        <el-button class="search_btn" slot="suffix" icon="el-icon-search" @click="searchGraph"></el-button>
      </el-input>

      <Avatar></Avatar>
    </div>

    <el-dialog
      title="创建新图谱"
      :visible.sync="CreateChartVisible"
      width="30%">
               <span>
                 <el-input v-model="input_chart" placeholder="请输入图谱名"></el-input>
               </span>
      <span>

             </span>
      <span slot="footer" class="dialog-footer">
               <el-button @click="CreateChartVisible = false">取 消</el-button>
               <el-button type="primary" @click="handleCreateChartClose">确 定</el-button>
               </span>
    </el-dialog>


    <div>
      <div class="buttonHolder">
        <el-button id="btn_P" class="btn_PublicGraph" @click="PublicGraph"></el-button>
        <div id="Name_P" style="float: left; font-size: 10px;height: 15px;margin-left: 5px;color: #f3f6fd;"
             v-model="bolPublicGraph"><strong>公共图谱</strong></div>
        <el-button id="btn_W" class="btn_Warehouse" @click="Warehouse"></el-button>
        <div id="Name_W" style="float: left; font-size: 10px;height: 15px;margin-left: 5px" v-model="bolPublicGraph">
          <strong>个人仓库</strong></div>
        <!--        <a href="#" class="button1 tick" style="margin-top: 20px"></a>-->
        <a href="#" class="button1 tick" style="margin-top: 20px" v-show="this.Modal==='Warehouse'"
           @click="CreateChartVisible=true"></a>
      </div>

      <div class="PublicMode" v-show="this.Modal==='publicGraph'">
        <div>
          <div class="PublicGraph">
            <FourGraphs></FourGraphs>
          </div>
        </div>
        <div style="margin-left: 940px">
          <div class="recommend">
<!--            <img class="recommend_img"-->
<!--                 src="https://lililizi.oss-cn-beijing.aliyuncs.com/a_%E8%BD%AF%E5%B7%A53/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20210609155511.png">-->
            <stock-card v-if="stock_card_update" class="recommend_items"></stock-card>
<!--            <el-popover-->
<!--              title="设置刷新条件"-->
<!--              placement="bottom-start"-->
<!--              width="210"-->
<!--              trigger="click"-->
<!--              v-model="StockRefresh_Visible"-->
<!--            >-->
<!--              <div>-->
<!--                <el-input v-model="StockExpectInput" placeholder="您的预期最低收益率:%" size="small"-->
<!--                          style="width: 203px"></el-input>-->
<!--              </div>-->
<!--              <div>-->
<!--                <el-input v-model="Stock_radio" placeholder="您的预期亏损容忍率:%" size="small" style="width: 203px"></el-input>-->
<!--              </div>-->
<!--              <div>-->
<!--                <el-radio-group v-model="userAcceptST" size="small">-->
<!--                  <el-radio-button label="是否接受ST" :disabled=true></el-radio-button>-->
<!--                  <el-radio-button label="接受"></el-radio-button>-->
<!--                  <el-radio-button label="拒绝"></el-radio-button>-->
<!--                </el-radio-group>-->
<!--              </div>-->
<!--              &lt;!&ndash;          <div >&ndash;&gt;-->
<!--              &lt;!&ndash;            <el-radio-group v-model="Stock_radio" style="margin-top: 10px" size="small" >&ndash;&gt;-->
<!--              &lt;!&ndash;              <el-radio-button label="低风险"></el-radio-button>&ndash;&gt;-->
<!--              &lt;!&ndash;              <el-radio-button label="中风险"></el-radio-button>&ndash;&gt;-->
<!--              &lt;!&ndash;              <el-radio-button label="高风险"></el-radio-button>&ndash;&gt;-->
<!--              &lt;!&ndash;            </el-radio-group>&ndash;&gt;-->
<!--              &lt;!&ndash;          </div>&ndash;&gt;-->
<!--              <div style="float: right;margin-top: 10px">-->
<!--                <el-button size="mini" type="text" @click="StockRefresh_Visible = false">取消</el-button>-->
<!--                <el-button type="primary" size="mini" @click.native="RefreshStock">确定</el-button>-->
<!--              </div>-->
<!--              <el-button slot="reference"-->
<!--                         class="reRecommend_button"-->
<!--                         icon="el-icon-refresh-left" circle></el-button>-->
<!--            </el-popover>-->
          </div>
          <div class="chatBot">
            <ChatLog></ChatLog>
          </div>
        </div>
      </div>

      <div class="body" v-show="this.Modal==='Warehouse'">
        <GraphCard v-if="update" style="margin-top: 15px;margin-bottom: 20px;" ref="GraphCard"></GraphCard>
      </div>
    </div>


  </div>
</template>

<script>
  import {mapGetters, mapMutations, mapActions} from 'vuex'
  import Avatar from '../Graphs/Components/Avatar'
  import GraphCard from './graphCard'
  import ChatLog from './Components/ChatLog'
  import stockCard from "./stockCard";
  import FourGraphs from './Components/FourGraphs'

  export default {
    name: "home",
    components: {
      Avatar,
      GraphCard,
      ChatLog,
      stockCard,
      FourGraphs
    },
    computed: {
      ...mapGetters([
        'userParams',
        'userGraphList',
      ])
    },
    async mounted() {
      this.changePic();
    },

    data() {
      return {
        bolWarehouseBtn: false,
        bolPublicGraph: true,
        searchInput: '',
        clearSearch: '',
        Modal: 'publicGraph',

        CreateChartVisible: false,
        // 图谱名
        input_chart: '',

        update: true,
        stock_card_update: true,

        StockRefresh_Visible: false,
        Stock_radio: "",
        StockExpectInput: "",
        userAcceptST: "",
      }
    },
    methods: {
      ...mapMutations([
        'set_userParams',
        'set_customGraphParams',
        'set_customGraphParamsClear',
      ]),

      ...mapActions([
        'getUserGraphList',
        'addGraph',
        'editUserParams',
      ]),


      PublicGraph() {
        if (this.bolPublicGraph === 'true') return;
        this.Modal = 'publicGraph';
        this.bolPublicGraph = true;
        this.bolWarehouseBtn = false;
        var obj1 = document.getElementById("btn_P");
        obj1.style.cssText = 'background-image: url("https://lililizi.oss-cn-beijing.aliyuncs.com/a_%E8%BD%AF%E5%B7%A53/%E5%9B%BE%E7%89%8712.png") ;background-size:55px;margin-left: 0;margin-top:4px;height:65px;width:65px';
        var obj2 = document.getElementById("btn_W");
        obj2.style.cssText = 'background-image: url("https://lililizi.oss-cn-beijing.aliyuncs.com/a_%E8%BD%AF%E5%B7%A53/%E6%88%BF%E5%AD%90%20%282%29.png") ;background-size:55px;margin-left: 0;margin-top:4px;height:65px;width:65px';
        var obj3 = document.getElementById("Name_W");
        obj3.style.cssText = 'float: left; font-size: 10px;height: 15px;margin-left: 5px;color: #f3f6fd;';
        var obj4 = document.getElementById("Name_P");
        obj4.style.cssText = 'float: left; font-size: 10px;height: 15px;margin-left: 5px;color: #000000;';
      },
      Warehouse() {
        if (this.bolWarehouseBtn === 'true') return;
        this.Modal = "Warehouse";
        this.bolWarehouseBtn = true;
        this.bolPublicGraph = false;
        var obj1 = document.getElementById("btn_W");
        obj1.style.cssText = 'background-image: url("https://lililizi.oss-cn-beijing.aliyuncs.com/a_%E8%BD%AF%E5%B7%A53/%E5%9B%BE%E7%89%8711.png") ;background-size:55px;margin-left: 0;margin-top:4px;height:65px;width:65px';
        var obj2 = document.getElementById("btn_P");
        obj2.style.cssText = 'background-image: url("https://lililizi.oss-cn-beijing.aliyuncs.com/a_%E8%BD%AF%E5%B7%A53/%E6%95%B0%E6%8D%AE%E5%9B%BE%E8%B0%B1.png") ;background-size:55px;margin-left: 0;margin-top:4px;height:65px;width:65px';
        var obj3 = document.getElementById("Name_P");
        obj3.style.cssText = 'float: left; font-size: 10px;height: 15px;margin-left: 5px;color: #f3f6fd;';
        var obj4 = document.getElementById("Name_W");
        obj4.style.cssText = 'float: left; font-size: 10px;height: 15px;margin-left: 5px;color: #000000;';

      },
      searchGraph() {
        if (this.searchInput != '') {
          this.$refs.GraphCard.resetGraphList();
          this.$refs.GraphCard.resetbolSearch();
          this.$refs.GraphCard.search(this.searchInput);
        }
      },
      clearSearch1() {
        this.searchInput = '';
        this.$refs.GraphCard.resetGraphList();
        this.$refs.GraphCard.resetbolSearch();
      },

      changePic() {
        for (let i = 0; i < this.userGraphList.length; i++) {
          if (this.userGraphList[i].id === this.$route.params.GraphId) {
            let tempPic = this.userGraphList[i];
            tempPic.url = this.$route.params.dataUrl;
            tempPic.time = this.$route.params.dataTime;
            this.$set(this.userGraphList, i, tempPic);
            break;
          }
        }
      },

      async handleCreateChartClose() {
        this.set_customGraphParamsClear({
          name: this.input_chart,
          url: "https://lililizi.oss-cn-beijing.aliyuncs.com/%E5%9B%BE%E7%89%871.png",
          time: new Date().toLocaleDateString()
        });
        await this.addGraph();

        this.update = false;
        this.input_chart = '';
        this.CreateChartVisible = false;
        this.$nextTick(() => {
          this.update = true
        })
      },

      refreshGraph() {
        this.update = false;
        this.$nextTick(() => {
          this.update = true
        })
      },

      refreshStockCard() {
        this.stock_card_update = false;
        this.$nextTick(() => {
          this.stock_card_update = true
        })
      },

      async RefreshStock() {
        this.StockRefresh_Visible = false;
        let acceptSt = '';
        if(this.userAcceptST==='接受'){
          acceptSt = true;
        }else if(this.userAcceptST==='拒绝'){
          acceptSt = false;
        }else{
          this.$message.error('错误的输入')
        }
        this.set_userParams({
          risk: this.Stock_radio,
          balance: this.StockExpectInput,
          acceptSt: acceptSt
        })
        await this.editUserParams()
        console.log(this.userParams)
        await this.refreshStockCard()
      }
    },
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

  .inline-input {
    float: left;
    margin-left: 23px;
    margin-top: 10px;
    width: 31%;
    border-radius: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.06), 0 0 6px rgba(0, 0, 0, .04)
  }

  .inline-input >>> .el-input__inner {
    border-radius: 20px;
  }

  .search_btn {
    margin-top: 2px;
    background-color: Transparent;
    border-style: none;
  }

  .body {
    float: right;
    width: 90%;
    margin-top: 10px;
    margin-right: 10px;
    background: white;
    border-radius: 35px;
  }

  /*.recommend {*/
  /*  position: absolute;*/
  /*}*/

  /*.recommend_img {*/
  /*  margin-top: 20px;*/
  /*  width: 50px;*/
  /*  height: 50px;*/
  /*  float: left;*/
  /*  margin-right: 10px*/
  /*}*/

  /*.recommend_items {*/
  /*  width: 600px;*/
  /*  margin-top: 10px;*/
  /*  float: left*/
  /*}*/

  /*.reRecommend_button {*/
  /*  float: left;*/
  /*  width: 40px;*/
  /*  height: 40px;*/
  /*  margin-left: 20px;*/
  /*  font-size: 35px;*/
  /*  background-repeat: no-repeat;*/
  /*  background-color: transparent;*/
  /*  border: 0 none;*/
  /*}*/

  .chatBot {
    position: absolute;
    margin-left: 12%;
    margin-top: 200px;
    width: 500px;
    background-color: white;

  }

  .PublicMode {
    width: 1550px;
    height: 700px;
    float: left;
  }

  .PublicGraph {
    margin-top: 10px;
    float: left;
    width: 110%;
    height: 800px;
    background: white;
    border-radius: 35px;
    margin-left: 10px;
  }

  .buttonHolder {
    float: left;
    width: 70px;
    margin-left: 20px;
    margin-top: 25px;
  }

  .button2 {
    background-image: -webkit-linear-gradient(top, #f4f1ee, #fff);
    background-image: linear-gradient(top, #f4f1ee, #fff);
    border-radius: 50%;
    box-shadow: 0px 8px 10px 0px rgba(109, 109, 109, 0.3), inset 0px 4px 1px 1px white, inset 0px -3px 1px 1px rgba(204, 198, 197, .5);
    float: left;
    height: 50px;
    margin: 0 30px 30px 0;
    position: relative;
    width: 50px;
    -webkit-transition: all .1s linear;
    transition: all .1s linear;
  }

  .button1 {
    background-image: -webkit-linear-gradient(top, #f4f1ee, #fff);
    background-image: linear-gradient(top, #f4f1ee, #fff);
    border-radius: 15%;
    box-shadow: 0px 8px 10px 0px rgba(109, 109, 109, 0.07), inset 0px 4px 1px 1px white, inset 0px -3px 1px 1px rgba(204, 198, 197, .5);
    float: left;
    height: 50px;
    margin: 0 30px 30px 0;
    position: relative;
    width: 50px;
    -webkit-transition: all .1s linear;
    transition: all .1s linear;
  }

  .button1:after {
    color: #e9e6e4;
    content: "";
    display: block;
    font-size: 30px;
    height: 30px;
    text-decoration: none;
    text-shadow: 0px -1px 1px #bdb5b4, 1px 1px 1px white;
    position: absolute;
    width: 30px;
  }

  .tick:after {
    content: "＋";
    left: 11px;
    top: 4px;
  }


  .button1:hover {
    background-image: -webkit-linear-gradient(top, #fff, #f4f1ee);
    background-image: linear-gradient(top, #fff, #f4f1ee);
    color: #0088cc;
  }

  .tick:hover:after {
    color: rgba(0, 0, 0, 0.57);
    text-shadow: 0px 0px 6px rgba(0, 0, 0, 0.56);
  }


  .button1:active {
    background-image: -webkit-linear-gradient(top, #efedec, #f7f4f4);
    background-image: linear-gradient(top, #efedec, #f7f4f4);
    box-shadow: 0 3px 5px 0 rgba(0, 0, 0, .4), inset 0px -3px 1px 1px rgba(204, 198, 197, .5);
  }

  .button1:active:after {
    color: #dbd2d2;
    text-shadow: 0px -1px 1px #bdb5b4, 0px 1px 1px white;
  }


  .btn_PublicGraph {
    float: left;
    background-repeat: no-repeat;
    border: 0;
    background-color: transparent;
    background-image: url("https://lililizi.oss-cn-beijing.aliyuncs.com/a_%E8%BD%AF%E5%B7%A53/%E5%9B%BE%E7%89%8712.png");
    border-style: none;
    background-size: 55px;
    margin-left: 0;
    margin-top: 4px;
    height: 65px;
    width: 65px;
  }

  .btn_Warehouse {
    float: left;
    background-repeat: no-repeat;
    border: 0;
    background-color: transparent;
    border-style: none;
    background-image: url("https://lililizi.oss-cn-beijing.aliyuncs.com/a_%E8%BD%AF%E5%B7%A53/%E6%88%BF%E5%AD%90%20%282%29.png");
    background-size: 55px;
    margin-left: 0;
    margin-top: 4px;
    height: 65px;
    width: 65px;
  }

</style>
