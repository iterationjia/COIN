<template>
  <div>
    <el-select v-model="graphValue" class="selectBoard" @change="resetGraph" placeholder="请选择感兴趣的行业">
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value">
      </el-option>
    </el-select>
    <div class="bodyGraphContainer"></div>
  </div>
</template>

<script>
  import {mapGetters, mapMutations, mapActions} from 'vuex';
  import * as d3 from 'd3'

  export default {
    name: "FourGraphs",
    mounted() {

    },
    computed: {
      ...mapGetters([])
    },
    data() {
      return {
        graphValue: '',
        options: [{
          value: '1',
          label: '信息传输、软件和信息技术服务业'
        }, {
          value: '2',
          label: '房地产业'
        }, {
          value: '3',
          label: '文化、体育、娱乐业'
        }],

        svgArea: null,
        width: 850,
        height: 400,
        links: [],
        nodes: [],
        rectNodes: [], //方形节点
        triangleUpNodes: [], //三角形节点
        diamondNodes: [], //菱形节点
        nodesName: [],
        linksName: [],
        simulation: null,
        currentData: {},
        testData1: {
          "nodes": [
            {
              "id": "15039",
              "name": "互联网相关服务",
              "color": "#FF8000"
            }, {
              "id": "3884",
              "name": "湖北盛天网络技术股份有限公司",
              "color": "#A52A2A"
            }, {
              "id": "16560",
              "name": "盛天网络",
              "color": "#00FFFF"
            }, {
              "id": "14737",
              "name": "赖春林",
              "color": "#00FF00"
            }, {
              "id": "20081",
              "name": "300494.XSHE的价格指数",
              "color": "#FF00FF"
            }, {
              "id": "3792",
              "name": "北京全时天地在线网络信息股份有限公司",
              "color": "#A52A2A"
            }, {
              "id": "13529",
              "name": "信意安",
              "color": "#00FF00"
            }, {
              "id": "18353",
              "name": "天地在线",
              "color": "#00FFFF"
            }, {
              "id": "21110",
              "name": "002995.XSHE的价格指数",
              "color": "#FF00FF"
            }, {
              "id": "3632",
              "name": "星辉互动娱乐股份有限公司",
              "color": "#A52A2A"
            }, {
              "id": "5075",
              "name": "陈创煌",
              "color": "#00FF00"
            }, {
              "id": "14112",
              "name": "陈雁升",
              "color": "#00FF00"
            }, {
              "id": "15236",
              "name": "星辉娱乐",
              "color": "#00FFFF"
            }, {
              "id": "20395",
              "name": "300043.XSHE的价格指数",
              "color": "#FF00FF"
            }, {
              "id": "3565",
              "name": "南凌科技股份有限公司",
              "color": "#A52A2A"
            }, {
              "id": "10957",
              "name": "陈树林",
              "color": "#00FF00"
            }, {
              "id": "17554",
              "name": "南凌科技",
              "color": "#00FFFF"
            }, {
              "id": "22489",
              "name": "300921.XSHE的价格指数",
              "color": "#FF00FF"
            }, {
              "id": "15067",
              "name": "信息传输、软件和信息技术服务业",
              "color": "#FF0000"
            }, {
              "id": "3865",
              "name": "巨人网络集团股份有限公司",
              "color": "#A52A2A"
            }, {
              "id": "17342",
              "name": "巨人网络",
              "color": "#00FFFF"
            }, {
              "id": "23352",
              "name": "002558.XSHE的价格指数",
              "color": "#FF00FF"
            }, {
              "id": "4222",
              "name": "刘伟",
              "color": "#00FF00"
            }, {
              "id": "3838",
              "name": "拉卡拉支付股份有限公司",
              "color": "#A52A2A"
            }, {
              "id": "10297",
              "name": "陈烈",
              "color": "#00FF00"
            }, {
              "id": "2883",
              "name": "云南南天电子信息产业股份有限公司",
              "color": "#A52A2A"
            }, {
              "id": "16306",
              "name": "南天信息",
              "color": "#00FFFF"
            }, {
              "id": "19270",
              "name": "000948.XSHE的价格指数",
              "color": "#FF00FF"
            }, {
              "id": "2997",
              "name": "德展大健康股份有限公司",
              "color": "#A52A2A"
            }, {
              "id": "16744",
              "name": "德展健康",
              "color": "#00FFFF"
            }, {
              "id": "20517",
              "name": "000813.XSHE的价格指数",
              "color": "#FF00FF"
            }, {
              "id": "3025",
              "name": "威龙葡萄酒股份有限公司",
              "color": "#A52A2A"
            }, {
              "id": "16904",
              "name": "ST威龙",
              "color": "#00FFFF"
            }, {
              "id": "22255",
              "name": "603779.XSHG的价格指数",
              "color": "#FF00FF"
            }, {
              "id": "2603",
              "name": "广东风华高新科技股份有限公司",
              "color": "#A52A2A"
            }, {
              "id": "16704",
              "name": "风华高科",
              "color": "#00FFFF"
            }, {
              "id": "19570",
              "name": "000636.XSHE的价格指数",
              "color": "#FF00FF"
            }
          ],
          "links": [
            {
              "id": "15422",
              "name": "行业二级分类",
              "source": "15039",
              "target": "3884"
            }, {
              "id": "8015",
              "name": "法人",
              "source": "3884",
              "target": "14737"
            }, {
              "id": "3856",
              "name": "ceo",
              "source": "14737",
              "target": "3884"
            }, {
              "id": "18017",
              "name": "所属公司",
              "source": "16560",
              "target": "3884"
            }, {
              "id": "21401",
              "name": "价格指数",
              "source": "16560",
              "target": "20081"
            }, {
              "id": "15330",
              "name": "行业二级分类",
              "source": "15039",
              "target": "3792"
            }, {
              "id": "7872",
              "name": "法人",
              "source": "3792",
              "target": "13529"
            }, {
              "id": "3764",
              "name": "ceo",
              "source": "3792",
              "target": "13529"
            }, {
              "id": "16982",
              "name": "所属公司",
              "source": "18353",
              "target": "3792"
            }, {
              "id": "23194",
              "name": "价格指数",
              "source": "18353",
              "target": "21110"
            }, {
              "id": "15170",
              "name": "行业二级分类",
              "source": "15039",
              "target": "3632"
            }, {
              "id": "3604",
              "name": "ceo",
              "source": "3632",
              "target": "5075"
            }, {
              "id": "9063",
              "name": "高管",
              "source": "3632",
              "target": "5075"
            }, {
              "id": "9065",
              "name": "高管",
              "source": "3632",
              "target": "14112"
            }, {
              "id": "7766",
              "name": "法人",
              "source": "3632",
              "target": "14112"
            }, {
              "id": "15476",
              "name": "所属公司",
              "source": "15236",
              "target": "3632"
            }, {
              "id": "20078",
              "name": "价格指数",
              "source": "15236",
              "target": "20395"
            }, {
              "id": "15103",
              "name": "行业二级分类",
              "source": "15039",
              "target": "3565"
            }, {
              "id": "7699",
              "name": "法人",
              "source": "3565",
              "target": "10957"
            }, {
              "id": "3537",
              "name": "ceo",
              "source": "3565",
              "target": "10957"
            }, {
              "id": "16257",
              "name": "所属公司",
              "source": "17554",
              "target": "3565"
            }, {
              "id": "22395",
              "name": "价格指数",
              "source": "17554",
              "target": "22489"
            }, {
              "id": "15769",
              "name": "行业一级分类",
              "source": "15067",
              "target": "15039"
            }, {
              "id": "15403",
              "name": "行业二级分类",
              "source": "15039",
              "target": "3865"
            }, {
              "id": "19131",
              "name": "所属公司",
              "source": "17342",
              "target": "3865"
            }, {
              "id": "22183",
              "name": "价格指数",
              "source": "17342",
              "target": "23352"
            }, {
              "id": "3837",
              "name": "ceo",
              "source": "3865",
              "target": "4222"
            }, {
              "id": "7996",
              "name": "法人",
              "source": "3865",
              "target": "4222"
            }, {
              "id": "15376",
              "name": "行业二级分类",
              "source": "15039",
              "target": "3838"
            }, {
              "id": "3810",
              "name": "ceo",
              "source": "3838",
              "target": "10297"
            }, {
              "id": "10437",
              "name": "高管",
              "source": "2883",
              "target": "10297"
            }, {
              "id": "10430",
              "name": "高管",
              "source": "2883",
              "target": "4222"
            }, {
              "id": "15867",
              "name": "所属公司",
              "source": "16306",
              "target": "2883"
            }, {
              "id": "21147",
              "name": "价格指数",
              "source": "16306",
              "target": "19270"
            }, {
              "id": "2959",
              "name": "ceo",
              "source": "2997",
              "target": "4222"
            }, {
              "id": "16291",
              "name": "所属公司",
              "source": "16744",
              "target": "2997"
            }, {
              "id": "21585",
              "name": "价格指数",
              "source": "16744",
              "target": "20517"
            }, {
              "id": "11041",
              "name": "高管",
              "source": "3025",
              "target": "4222"
            }, {
              "id": "15865",
              "name": "所属公司",
              "source": "16904",
              "target": "3025"
            }, {
              "id": "21745",
              "name": "价格指数",
              "source": "16904",
              "target": "22255"
            }, {
              "id": "6732",
              "name": "法人",
              "source": "2603",
              "target": "4222"
            }, {
              "id": "19399",
              "name": "所属公司",
              "source": "16704",
              "target": "2603"
            }, {
              "id": "21545",
              "name": "价格指数",
              "source": "16704",
              "target": "19570"
            }
          ]
        },
        testData2: {
          "nodes": [
            {
              "id": 20605,
              "name": "房地产业",
              "color": "#FF0000"
            },
            {
              "id": 20525,
              "name": "房地产业",
              "color": "#FF8000"
            },
            {
              "id": 8775,
              "name": "泰禾集团股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 9009,
              "name": "广东世荣兆业股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 9106,
              "name": "深圳市新南山控股(集团)股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 8167,
              "name": "天津久日新材料股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 7764,
              "name": "宁夏嘉泽新能源股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 7183,
              "name": "吴通控股集团股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 8439,
              "name": "云南南天电子信息产业股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 9614,
              "name": "华联控股股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 9641,
              "name": "深圳市全新好股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 9642,
              "name": "深圳市振业(集团)股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 8710,
              "name": "中交地产股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 9647,
              "name": "沙河实业股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 8700,
              "name": "美好置业集团股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 8888,
              "name": "南都物业服务集团股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 8762,
              "name": "苏宁环球股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 8175,
              "name": "安徽古井贡酒股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 8814,
              "name": "江西黑猫炭黑股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 9524,
              "name": "潜江永安药业股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 7580,
              "name": "桂林福达股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 6658,
              "name": "四川长虹电器股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 5736,
              "name": "兖州煤业股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 7619,
              "name": "飞天诚信科技股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 8628,
              "name": "北京交大思诺科技股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": 22326,
              "name": "久日新材",
              "color": "#00FFFF"
            },
            {
              "id": 21998,
              "name": "嘉泽新能",
              "color": "#00FFFF"
            },
            {
              "id": 22996,
              "name": "世荣兆业",
              "color": "#00FFFF"
            },
            {
              "id": 22905,
              "name": "南山控股",
              "color": "#00FFFF"
            },
            {
              "id": 24445,
              "name": "泰禾集团",
              "color": "#00FFFF"
            },
            {
              "id": 24401,
              "name": "古井贡酒",
              "color": "#00FFFF"
            },
            {
              "id": 24214,
              "name": "苏宁环球",
              "color": "#00FFFF"
            },
            {
              "id": 22853,
              "name": "南都物业",
              "color": "#00FFFF"
            },
            {
              "id": 24468,
              "name": "美好置业",
              "color": "#00FFFF"
            },
            {
              "id": 23794,
              "name": "中交地产",
              "color": "#00FFFF"
            },
            {
              "id": 24531,
              "name": "黑猫股份",
              "color": "#00FFFF"
            },
            {
              "id": 22471,
              "name": "永安药业",
              "color": "#00FFFF"
            },
            {
              "id": 21017,
              "name": "福大股份",
              "color": "#00FFFF"
            },
            {
              "id": 21487,
              "name": "四川长虹",
              "color": "#00FFFF"
            },
            {
              "id": 24449,
              "name": "兖州煤业",
              "color": "#00FFFF"
            },
            {
              "id": 22982,
              "name": "飞天诚信",
              "color": "#00FFFF"
            },
            {
              "id": 22669,
              "name": "交大思诺",
              "color": "#00FFFF"
            },
            {
              "id": 24770,
              "name": "*ST全新",
              "color": "#00FFFF"
            },
            {
              "id": 21857,
              "name": "南天信息",
              "color": "#00FFFF"
            },
            {
              "id": 22370,
              "name": "吴通控股",
              "color": "#00FFFF"
            },
            {
              "id": 11481,
              "name": "李绪鹏",
              "color": "#00FF00"
            },
            {
              "id": 15133,
              "name": "陈波",
              "color": "#00FF00"
            },
            {
              "id": 10062,
              "name": "王世云",
              "color": "#00FF00"
            },
            {
              "id": 17755,
              "name": "张建国",
              "color": "#00FF00"
            },
            {
              "id": 18517,
              "name": "田俊彦",
              "color": "#00FF00"
            },
            {
              "id": 10052,
              "name": "黄其森",
              "color": "#00FF00"
            },
            {
              "id": 12715,
              "name": "李云",
              "color": "#00FF00"
            },
            {
              "id": 15091,
              "name": "陆波",
              "color": "#00FF00"
            },
            {
              "id": 19450,
              "name": "李永前",
              "color": "#00FF00"
            },
            {
              "id": 12810,
              "name": "李伟",
              "color": "#00FF00"
            },
            {
              "id": 17189,
              "name": "赵宏伟",
              "color": "#00FF00"
            },
            {
              "id": 10723,
              "name": "陈勇",
              "color": "#00FF00"
            },
            {
              "id": 12095,
              "name": "刘道明",
              "color": "#00FF00"
            },
            {
              "id": 20159,
              "name": "王耀",
              "color": "#00FF00"
            },
            {
              "id": 16541,
              "name": "韩芳",
              "color": "#00FF00"
            },
            {
              "id": 13314,
              "name": "张桂平",
              "color": "#00FF00"
            },
            {
              "id": 26722,
              "name": "300292.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 28886,
              "name": "688199.XSHG的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 24809,
              "name": "000948.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 27291,
              "name": "002314.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 25215,
              "name": "002016.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 25203,
              "name": "000732.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 28862,
              "name": "000718.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 26058,
              "name": "000596.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 25264,
              "name": "603506.XSHG的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 27302,
              "name": "002068.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 28421,
              "name": "000667.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 26047,
              "name": "000736.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 25218,
              "name": "002365.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 27792,
              "name": "603166.XSHG的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 24904,
              "name": "600839.XSHG的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 27115,
              "name": "600188.XSHG的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 24840,
              "name": "300386.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 26679,
              "name": "300851.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 27516,
              "name": "000007.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": 26423,
              "name": "601619.XSHG的价格指数",
              "color": "#FF00FF"
            }
          ],
          "links": [
            {
              "id": 19866,
              "source": 20605,
              "target": 20525,
              "name": "行业一级分类"
            },
            {
              "id": 19724,
              "source": 20525,
              "target": 9647,
              "name": "行业二级分类"
            },
            {
              "id": 12316,
              "source": 9647,
              "target": 10723,
              "name": "法人"
            },
            {
              "id": 12193,
              "source": 9524,
              "target": 10723,
              "name": "法人"
            },
            {
              "id": 22153,
              "source": 22471,
              "target": 9524,
              "name": "所属公司"
            },
            {
              "id": 25863,
              "source": 22471,
              "target": 25218,
              "name": "价格指数"
            },
            {
              "id": 19719,
              "source": 20525,
              "target": 9642,
              "name": "行业二级分类"
            },
            {
              "id": 12311,
              "source": 9642,
              "target": 17189,
              "name": "法人"
            },
            {
              "id": 1906,
              "source": 7580,
              "target": 17189,
              "name": "ceo"
            },
            {
              "id": 23053,
              "source": 21017,
              "target": 7580,
              "name": "所属公司"
            },
            {
              "id": 24409,
              "source": 21017,
              "target": 27792,
              "name": "价格指数"
            },
            {
              "id": 3968,
              "source": 9642,
              "target": 12810,
              "name": "ceo"
            },
            {
              "id": 12906,
              "source": 5736,
              "target": 12810,
              "name": "高管"
            },
            {
              "id": 22403,
              "source": 24449,
              "target": 5736,
              "name": "所属公司"
            },
            {
              "id": 27841,
              "source": 24449,
              "target": 27115,
              "name": "价格指数"
            },
            {
              "id": 11297,
              "source": 8628,
              "target": 12810,
              "name": "法人"
            },
            {
              "id": 20207,
              "source": 22669,
              "target": 8628,
              "name": "所属公司"
            },
            {
              "id": 26061,
              "source": 22669,
              "target": 26679,
              "name": "价格指数"
            },
            {
              "id": 1945,
              "source": 7619,
              "target": 12810,
              "name": "ceo"
            },
            {
              "id": 22045,
              "source": 22982,
              "target": 7619,
              "name": "所属公司"
            },
            {
              "id": 26374,
              "source": 22982,
              "target": 24840,
              "name": "价格指数"
            },
            {
              "id": 984,
              "source": 6658,
              "target": 12810,
              "name": "ceo"
            },
            {
              "id": 23302,
              "source": 21487,
              "target": 6658,
              "name": "所属公司"
            },
            {
              "id": 24879,
              "source": 21487,
              "target": 24904,
              "name": "价格指数"
            },
            {
              "id": 19718,
              "source": 20525,
              "target": 9641,
              "name": "行业二级分类"
            },
            {
              "id": 12310,
              "source": 9641,
              "target": 15091,
              "name": "法人"
            },
            {
              "id": 3967,
              "source": 9641,
              "target": 15091,
              "name": "ceo"
            },
            {
              "id": 20761,
              "source": 24770,
              "target": 9641,
              "name": "所属公司"
            },
            {
              "id": 28162,
              "source": 24770,
              "target": 27516,
              "name": "价格指数"
            },
            {
              "id": 19691,
              "source": 20525,
              "target": 9614,
              "name": "行业二级分类"
            },
            {
              "id": 3940,
              "source": 9614,
              "target": 12715,
              "name": "ceo"
            },
            {
              "id": 14526,
              "source": 8439,
              "target": 12715,
              "name": "高管"
            },
            {
              "id": 19962,
              "source": 21857,
              "target": 8439,
              "name": "所属公司"
            },
            {
              "id": 25249,
              "source": 21857,
              "target": 24809,
              "name": "价格指数"
            },
            {
              "id": 19183,
              "source": 20525,
              "target": 9106,
              "name": "行业二级分类"
            },
            {
              "id": 15345,
              "source": 9106,
              "target": 10062,
              "name": "高管"
            },
            {
              "id": 3432,
              "source": 9106,
              "target": 10062,
              "name": "ceo"
            },
            {
              "id": 22174,
              "source": 22905,
              "target": 9106,
              "name": "所属公司"
            },
            {
              "id": 26297,
              "source": 22905,
              "target": 27291,
              "name": "价格指数"
            },
            {
              "id": 15339,
              "source": 9106,
              "target": 17755,
              "name": "高管"
            },
            {
              "id": 1509,
              "source": 7183,
              "target": 17755,
              "name": "ceo"
            },
            {
              "id": 23623,
              "source": 22370,
              "target": 7183,
              "name": "所属公司"
            },
            {
              "id": 25762,
              "source": 22370,
              "target": 26722,
              "name": "价格指数"
            },
            {
              "id": 15335,
              "source": 9106,
              "target": 18517,
              "name": "高管"
            },
            {
              "id": 11775,
              "source": 9106,
              "target": 18517,
              "name": "法人"
            },
            {
              "id": 15334,
              "source": 9106,
              "target": 15133,
              "name": "高管"
            },
            {
              "id": 12912,
              "source": 8167,
              "target": 15133,
              "name": "高管"
            },
            {
              "id": 22644,
              "source": 22326,
              "target": 8167,
              "name": "所属公司"
            },
            {
              "id": 25718,
              "source": 22326,
              "target": 28886,
              "name": "价格指数"
            },
            {
              "id": 10433,
              "source": 7764,
              "target": 15133,
              "name": "法人"
            },
            {
              "id": 21558,
              "source": 21998,
              "target": 7764,
              "name": "所属公司"
            },
            {
              "id": 25390,
              "source": 21998,
              "target": 26423,
              "name": "价格指数"
            },
            {
              "id": 19086,
              "source": 20525,
              "target": 9009,
              "name": "行业二级分类"
            },
            {
              "id": 11678,
              "source": 9009,
              "target": 11481,
              "name": "法人"
            },
            {
              "id": 3335,
              "source": 9009,
              "target": 11481,
              "name": "ceo"
            },
            {
              "id": 21571,
              "source": 22996,
              "target": 9009,
              "name": "所属公司"
            },
            {
              "id": 26388,
              "source": 22996,
              "target": 25215,
              "name": "价格指数"
            },
            {
              "id": 18965,
              "source": 20525,
              "target": 8888,
              "name": "行业二级分类"
            },
            {
              "id": 11557,
              "source": 8888,
              "target": 16541,
              "name": "法人"
            },
            {
              "id": 3214,
              "source": 8888,
              "target": 16541,
              "name": "ceo"
            },
            {
              "id": 21307,
              "source": 22853,
              "target": 8888,
              "name": "所属公司"
            },
            {
              "id": 26245,
              "source": 22853,
              "target": 25264,
              "name": "价格指数"
            },
            {
              "id": 18852,
              "source": 20525,
              "target": 8775,
              "name": "行业二级分类"
            },
            {
              "id": 11444,
              "source": 8775,
              "target": 10052,
              "name": "法人"
            },
            {
              "id": 3101,
              "source": 8775,
              "target": 10052,
              "name": "ceo"
            },
            {
              "id": 22800,
              "source": 24445,
              "target": 8775,
              "name": "所属公司"
            },
            {
              "id": 27837,
              "source": 24445,
              "target": 25203,
              "name": "价格指数"
            },
            {
              "id": 18839,
              "source": 20525,
              "target": 8762,
              "name": "行业二级分类"
            },
            {
              "id": 11431,
              "source": 8762,
              "target": 13314,
              "name": "法人"
            },
            {
              "id": 12982,
              "source": 8175,
              "target": 13314,
              "name": "高管"
            },
            {
              "id": 21988,
              "source": 24401,
              "target": 8175,
              "name": "所属公司"
            },
            {
              "id": 27793,
              "source": 24401,
              "target": 26058,
              "name": "价格指数"
            },
            {
              "id": 3088,
              "source": 8762,
              "target": 13314,
              "name": "ceo"
            },
            {
              "id": 22904,
              "source": 24214,
              "target": 8762,
              "name": "所属公司"
            },
            {
              "id": 27606,
              "source": 24214,
              "target": 28862,
              "name": "价格指数"
            },
            {
              "id": 18787,
              "source": 20525,
              "target": 8710,
              "name": "行业二级分类"
            },
            {
              "id": 11379,
              "source": 8710,
              "target": 19450,
              "name": "法人"
            },
            {
              "id": 3036,
              "source": 8710,
              "target": 19450,
              "name": "ceo"
            },
            {
              "id": 22803,
              "source": 23794,
              "target": 8710,
              "name": "所属公司"
            },
            {
              "id": 27186,
              "source": 23794,
              "target": 26047,
              "name": "价格指数"
            },
            {
              "id": 18777,
              "source": 20525,
              "target": 8700,
              "name": "行业二级分类"
            },
            {
              "id": 15533,
              "source": 8700,
              "target": 20159,
              "name": "高管"
            },
            {
              "id": 11483,
              "source": 8814,
              "target": 20159,
              "name": "法人"
            },
            {
              "id": 21662,
              "source": 24531,
              "target": 8814,
              "name": "所属公司"
            },
            {
              "id": 27923,
              "source": 24531,
              "target": 27302,
              "name": "价格指数"
            },
            {
              "id": 15530,
              "source": 8700,
              "target": 12095,
              "name": "高管"
            },
            {
              "id": 11369,
              "source": 8700,
              "target": 12095,
              "name": "法人"
            },
            {
              "id": 23665,
              "source": 24468,
              "target": 8700,
              "name": "所属公司"
            },
            {
              "id": 27860,
              "source": 24468,
              "target": 28421,
              "name": "价格指数"
            }
          ]
        },
        testData3: {
          "nodes": [
            {
              "id": "16227",
              "name": "文化、体育和娱乐业",
              "color": "#FF0000"
            },
            {
              "id": "16192",
              "name": "广播、电视、电影和影视录音制作业",
              "color": "#FF8000"
            },
            {
              "id": "2797",
              "name": "北京华录百纳影视股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": "11428",
              "name": "方刚",
              "color": "#00FF00"
            },
            {
              "id": "16727",
              "name": "华录百纳",
              "color": "#00FFFF"
            },
            {
              "id": "22579",
              "name": "300291.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": "1764",
              "name": "文投控股股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": "11929",
              "name": "王森",
              "color": "#00FF00"
            },
            {
              "id": "2463",
              "name": "佛山市国星光电股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": "18051",
              "name": "国星光电",
              "color": "#00FFFF"
            },
            {
              "id": "23255",
              "name": "002449.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": "1822",
              "name": "北京光线传媒股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": "7726",
              "name": "王长田",
              "color": "#00FF00"
            },
            {
              "id": "16640",
              "name": "光线传媒",
              "color": "#00FFFF"
            },
            {
              "id": "21830",
              "name": "300251.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": "4230",
              "name": "欢瑞世纪联合股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": "7911",
              "name": "赵枳程",
              "color": "#00FF00"
            },
            {
              "id": "17767",
              "name": "欢瑞世纪",
              "color": "#00FFFF"
            },
            {
              "id": "21592",
              "name": "000892.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": "4014",
              "name": "上海电影股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": "7436",
              "name": "王健儿",
              "color": "#00FF00"
            },
            {
              "id": "16948",
              "name": "上海电影",
              "color": "#00FFFF"
            },
            {
              "id": "23862",
              "name": "601595.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": "3888",
              "name": "幸福蓝海影视集团股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": "8783",
              "name": "蒋小平",
              "color": "#00FF00"
            },
            {
              "id": "16424",
              "name": "幸福蓝海",
              "color": "#00FFFF"
            },
            {
              "id": "24530",
              "name": "300528.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": "3236",
              "name": "万达电影股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": "8078",
              "name": "曾茂军",
              "color": "#00FF00"
            },
            {
              "id": "14904",
              "name": "万达电影",
              "color": "#00FFFF"
            },
            {
              "id": "21402",
              "name": "002739.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": "3857",
              "name": "鼎龙文化股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": "17628",
              "name": "鼎龙文化",
              "color": "#00FFFF"
            },
            {
              "id": "22330",
              "name": "002502.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": "6797",
              "name": "杨芳",
              "color": "#00FF00"
            },
            {
              "id": "6050",
              "name": "龙学勤",
              "color": "#00FF00"
            },
            {
              "id": "5311",
              "name": "新希望乳业股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": "1834",
              "name": "汉商集团股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": "19082",
              "name": "新乳业",
              "color": "#00FFFF"
            },
            {
              "id": "19520",
              "name": "汉商集团",
              "color": "#00FFFF"
            },
            {
              "id": "23543",
              "name": "002946.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": "24561",
              "name": "600774.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": "2488",
              "name": "中南红文化集团股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": "16448",
              "name": "中南文化",
              "color": "#00FFFF"
            },
            {
              "id": "21200",
              "name": "002445.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": "9063",
              "name": "李华",
              "color": "#00FF00"
            },
            {
              "id": "8594",
              "name": "王哲",
              "color": "#00FF00"
            },
            {
              "id": "6477",
              "name": "王新立",
              "color": "#00FF00"
            },
            {
              "id": "7347",
              "name": "陈飞",
              "color": "#00FF00"
            },
            {
              "id": "7657",
              "name": "王辉",
              "color": "#00FF00"
            },
            {
              "id": "3773",
              "name": "天津锐新昌科技股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": "19296",
              "name": "锐新科技",
              "color": "#00FFFF"
            },
            {
              "id": "24407",
              "name": "300828.XSHE的价格指数",
              "color": "#FF00FF"
            },
            {
              "id": "3758",
              "name": "西王食品股份有限公司",
              "color": "#A52A2A"
            },
            {
              "id": "19360",
              "name": "西王食品",
              "color": "#00FFFF"
            },
            {
              "id": "21465",
              "name": "000639.XSHE的价格指数",
              "color": "#FF00FF"
            }
          ],
          "links": [
            {
              "id": "18223",
              "name": "行业一级分类",
              "source": "16227",
              "target": "16192"
            },
            {
              "id": "15632",
              "name": "行业二级分类",
              "source": "16192",
              "target": "2797"
            },
            {
              "id": "8224",
              "name": "法人",
              "source": "2797",
              "target": "11428"
            },
            {
              "id": "4065",
              "name": "ceo",
              "source": "2797",
              "target": "11428"
            },
            {
              "id": "21976",
              "name": "所属公司",
              "source": "16727",
              "target": "2797"
            },
            {
              "id": "22878",
              "name": "价格指数",
              "source": "16727",
              "target": "22579"
            },
            {
              "id": "14618",
              "name": "行业二级分类",
              "source": "16192",
              "target": "1764"
            },
            {
              "id": "3051",
              "name": "ceo",
              "source": "1764",
              "target": "11929"
            },
            {
              "id": "3750",
              "name": "ceo",
              "source": "2463",
              "target": "11929"
            },
            {
              "id": "22262",
              "name": "所属公司",
              "source": "18051",
              "target": "2463"
            },
            {
              "id": "24202",
              "name": "价格指数",
              "source": "18051",
              "target": "23255"
            },
            {
              "id": "14676",
              "name": "行业二级分类",
              "source": "16192",
              "target": "1822"
            },
            {
              "id": "7268",
              "name": "法人",
              "source": "1822",
              "target": "7726"
            },
            {
              "id": "3109",
              "name": "ceo",
              "source": "1822",
              "target": "7726"
            },
            {
              "id": "21517",
              "name": "所属公司",
              "source": "16640",
              "target": "1822"
            },
            {
              "id": "22791",
              "name": "价格指数",
              "source": "16640",
              "target": "21830"
            },
            {
              "id": "17065",
              "name": "行业二级分类",
              "source": "16192",
              "target": "4230"
            },
            {
              "id": "5498",
              "name": "ceo",
              "source": "4230",
              "target": "7911"
            },
            {
              "id": "9657",
              "name": "法人",
              "source": "4230",
              "target": "7911"
            },
            {
              "id": "13621",
              "name": "高管",
              "source": "4230",
              "target": "7911"
            },
            {
              "id": "18919",
              "name": "所属公司",
              "source": "17767",
              "target": "4230"
            },
            {
              "id": "23918",
              "name": "价格指数",
              "source": "17767",
              "target": "21592"
            },
            {
              "id": "16849",
              "name": "行业二级分类",
              "source": "16192",
              "target": "4014"
            },
            {
              "id": "12840",
              "name": "高管",
              "source": "4014",
              "target": "7436"
            },
            {
              "id": "9441",
              "name": "法人",
              "source": "4014",
              "target": "7436"
            },
            {
              "id": "20627",
              "name": "所属公司",
              "source": "16948",
              "target": "4014"
            },
            {
              "id": "23099",
              "name": "价格指数",
              "source": "16948",
              "target": "23862"
            },
            {
              "id": "16723",
              "name": "行业二级指南",
              "source": "16192",
              "target": "3888"
            },
            {
              "id": "9315",
              "name": "ceo",
              "source": "3888",
              "target": "8783"
            },
            {
              "id": "12117",
              "name": "高管",
              "source": "3888",
              "target": "8783"
            },
            {
              "id": "21059",
              "name": "所属公司",
              "source": "16424",
              "target": "3888"
            },
            {
              "id": "22569",
              "name": "价格指数",
              "source": "16424",
              "target": "24530"
            },
            {
              "id": "16071",
              "name": "行业二级分类",
              "source": "16192",
              "target": "3236"
            },
            {
              "id": "4504",
              "name": "ceo",
              "source": "3236",
              "target": "8078"
            },
            {
              "id": "8663",
              "name": "法人",
              "source": "3236",
              "target": "8078"
            },
            {
              "id": "18394",
              "name": "所属公司",
              "source": "14904",
              "target": "3236"
            },
            {
              "id": "22735",
              "name": "价格指数",
              "source": "14904",
              "target": "21402"
            },
            {
              "id": "16692",
              "name": "行业二级分类",
              "source": "16192",
              "target": "3857"
            },
            {
              "id": "11940",
              "name": "高管",
              "source": "3857",
              "target": "6050"
            },
            {
              "id": "9284",
              "name": "法人",
              "source": "3857",
              "target": "6050"
            },
            {
              "id": "5125",
              "name": "ceo",
              "source": "3857",
              "target": "6050"
            },
            {
              "id": "21303",
              "name": "所属公司",
              "source": "17628",
              "target": "3857"
            },
            {
              "id": "23779",
              "name": "价格指数",
              "source": "17628",
              "target": "22330"
            },
            {
              "id": "11942",
              "name": "高管",
              "source": "3857",
              "target": "6797"
            },
            {
              "id": "12808",
              "name": "高管",
              "source": "5311",
              "target": "6797"
            },
            {
              "id": "3121",
              "name": "ceo",
              "source": "1834",
              "target": "6797"
            },
            {
              "id": "19469",
              "name": "所属公司",
              "source": "19082",
              "target": "5311"
            },
            {
              "id": "25243",
              "name": "价格指数",
              "source": "19082",
              "target": "23543"
            },
            {
              "id": "22110",
              "name": "所属公司",
              "source": "19520",
              "target": "1834"
            },
            {
              "id": "25686",
              "name": "价格指数",
              "source": "19520",
              "target": "24561"
            },
            {
              "id": "15342",
              "name": "行业二级分类",
              "source": "16192",
              "target": "2488"
            },
            {
              "id": "22275",
              "name": "所属公司",
              "source": "16448",
              "target": "2488"
            },
            {
              "id": "22593",
              "name": "价格指数",
              "source": "16448",
              "target": "21200"
            },
            {
              "id": "13167",
              "name": "高管",
              "source": "2488",
              "target": "9063"
            },
            {
              "id": "13165",
              "name": "高管",
              "source": "2488",
              "target": "8594"
            },
            {
              "id": "11331",
              "name": "高管",
              "source": "3773",
              "target": "8594"
            },
            {
              "id": "18677",
              "name": "所属公司",
              "source": "19296",
              "target": "3773"
            },
            {
              "id": "25457",
              "name": "价格指数",
              "source": "19296",
              "target": "24407"
            },
            {
              "id": "3775",
              "name": "ceo",
              "source": "2488",
              "target": "6477"
            },
            {
              "id": "13173",
              "name": "高管",
              "source": "2488",
              "target": "6477"
            },
            {
              "id": "7934",
              "name": "法人",
              "source": "2488",
              "target": "7347"
            },
            {
              "id": "13163",
              "name": "高管",
              "source": "2488",
              "target": "7347"
            },
            {
              "id": "13169",
              "name": "高管",
              "source": "2488",
              "target": "7657"
            },
            {
              "id": "9185",
              "name": "法人",
              "source": "3758",
              "target": "7657"
            },
            {
              "id": "21862",
              "name": "所属公司",
              "source": "19360",
              "target": "3758"
            },
            {
              "id": "25526",
              "name": "价格指数",
              "source": "19360",
              "target": "21465"
            }
          ]
        },
      }
    },
    methods: {

      initGraph(data) {
        const links = data.links.map(d => Object.create(d));
        const nodes = data.nodes.map(d => Object.create(d));

        this.simulation = d3.forceSimulation(nodes)
          .force("link", d3.forceLink(links).id(d => d.id).distance(100))
          .force("collide", d3.forceCollide().radius(() => 50))
          .force("charge", d3.forceManyBody().strength(-40))
          .force("center", d3.forceCenter(this.width / 2, this.height / 2));

        this.svgArea = d3.select(".bodyGraphContainer")
          .append("svg")
          .attr("viewBox", [0, 0, this.width, this.height])
          .call(d3.zoom().on("zoom", function (event) {
            g.attr("transform", event.transform)
          }))

        this.addMarkers();

        const g = this.svgArea.append("g")
          .attr("class", "content");

        this.links = g.append("g")
          .selectAll("path")
          .data(links, function (d) {
            if (typeof (d.source) === 'object') {
              return d.source.id + "_" + d.name + "_" + d.target.id
            } else {
              return d.source + "_" + d.name + "_" + d.target
            }
          })
          .join("path")
          .attr("stroke", "#999")
          .attr("stroke-opacity", 0.8)
          .attr("marker-end", "url(resolved)")
          .attr("fill-opacity", 0)
          .attr("stroke-width", 2)
          .attr("class", "link")
          .attr("id", function (d) {
            if (typeof (d.source) === 'object') {
              return d.source.id + "_" + d.name + "_" + d.target.id
            } else {
              return d.source + "_" + d.name + "_" + d.target
            }
          })
          .on('mouseover', function (d) {
            d3.select(this).attr("stroke", "rgba(45,64,111,0.5)").attr("stroke-width", 4);
          }).on('mouseout', function (d) {
            d3.select(this).attr("stroke", "rgba(45,64,111,0.38)").attr("stroke-width", 2);
          });

        this.linksName = g.append("g")
          .selectAll("text")
          .data(links, function (d) {
            if (typeof (d.source) === 'object') {
              return d.source.id + "_" + d.name + "_" + d.target.id
            } else {
              return d.source + "_" + d.name + "_" + d.target
            }
          })
          .join("text")
          .style('text-anchor', 'middle')
          .style('font-size', '10px')
          .style('font-weight', 'bold')
          .append('textPath')
          .attr('xlink:href', function (d) {
            if (typeof (d.source) === 'object') {
              return "#" + d.source.id + "_" + d.name + "_" + d.target.id
            } else {
              return "#" + d.source + "_" + d.name + "_" + d.target
            }
          })
          .attr('startOffset', '50%')
          .text(d => d.name);

        this.nodes = g.append("g")
          .attr("stroke", "#999")
          .attr("stroke-width", 2)
          .selectAll("circle")
          .data(nodes)
          .join("circle")
          .attr("r", 30)
          .attr("class", "node")
          .attr("fill", function (d) {
            return d.color
          })
          .call(this.drag(this.simulation))
          .on('mouseover', function (d) {
            //当鼠标放在节点上时，高亮节点
            d3.select(this)
              .attr("stroke", "rgba(34,39,75,0.77)")
              .attr("stroke-width", 2.5);
          }).on('mouseout', function (d) {
            //当鼠标不再放在节点上时，恢复节点
            d3.select(this)
              .attr("stroke", "rgba(45,64,111,0.5)")
              .attr("stroke-width", 1);
          });

        this.nodes.append("title")
          .text(d => d.id);

        this.nodesName = g.append("g")
          .selectAll("text")
          .data(nodes)
          .join("text")
          .text(function (d) {
            return d.name
          })
          .attr("dx", -17.5)
          .attr("dy", 45)
          .attr("class", "node-name")
          .attr("fill", function (d) {
            return d.color
          });

        this.simulation.on("tick", () => {
          this.links
            .attr("d", function (d) {
              if (d.source.x < d.target.x) {
                return "M " + d.source.x + " " + d.source.y + " L " + d.target.x + " " + d.target.y
              } else if (d.source.x === d.target.x) {
                return "M" + (d.target.x + 20) + " " + (d.target.y - 10) + " C " + (d.target.x + 120) + " " + (d.target.y - 60)
                  + " " + (d.target.x + 120) + " " + (d.target.y + 60) + "," + (d.target.x + 20) + " " + (d.target.y + 10);
              } else {
                return "M " + d.target.x + " " + d.target.y + " L " + d.source.x + " " + d.source.y
              }
            })
            .attr("marker-end", function (d) {
              if (d.source.x < d.target.x) {
                return "url(#positiveMarker)"
              } else {
                return null
              }
            })
            .attr("marker-start", function (d) {
              if (d.source.x < d.target.x) {
                return null
              } else {
                return "url(#negativeMarker)"
              }
            })

          this.nodes
            .attr("cx", d => d.x)
            .attr("cy", d => d.y);

          this.nodesName
            .attr("x", function (d) {
              if (d.name.length > 1) {
                return d.x + 0.5 * 30 - this.getComputedTextLength() * 0.45
              } else {
                return d.x + 0.40 * 30
              }
            })
            .attr("y", d => d.y + 5);
        });
      },

      addMarkers() {
        const positiveMarker = this.svgArea.append("marker")
          .attr("id", "positiveMarker")
          .attr("orient", "auto")
          .attr("stroke-width", 2)
          .attr("markerUnits", "strokeWidth")
          .attr("markerUnits", "userSpaceOnUse")
          .attr("viewBox", "0 -5 10 10")
          .attr("refX", 35)
          .attr("refY", 0)
          .attr("markerWidth", 12)
          .attr("markerHeight", 12)
          .append("path")
          .attr("d", "M 0 -5 L 10 0 L 0 5")
          .attr('fill', '#999')
          .attr("stroke-opacity", 0.6);
        const negativeMarker = this.svgArea.append("marker")
          .attr("id", "negativeMarker")
          .attr("orient", "auto")
          .attr("stroke-width", 2)
          .attr("markerUnits", "strokeWidth")
          .attr("markerUnits", "userSpaceOnUse")
          .attr("viewBox", "0 -5 10 10")
          .attr("refX", -25)
          .attr("refY", 0)
          .attr("markerWidth", 12)
          .attr("markerHeight", 12)
          .append("path")
          .attr("d", "M 10 -5 L 0 0 L 10 5")
          .attr('fill', '#999')
          .attr("stroke-opacity", 0.6);
      },

      drag(simulation) {
        function dragstarted(event) {
          if (!event.active) simulation.alphaTarget(0.3).restart();
          event.subject.fx = event.subject.x;
          event.subject.fy = event.subject.y;
        }

        function dragged(event) {
          event.subject.fx = event.x;
          event.subject.fy = event.y;
        }

        function dragended(event) {
          if (!event.active) simulation.alphaTarget(0);
          event.subject.fx = null;
          event.subject.fy = null;
        }

        return d3.drag()
          .on("start", dragstarted)
          .on("drag", dragged)
          .on("end", dragended);
      },

      removeSvg() {
        console.log('remove!')
        d3.selectAll(".bodyGraphContainer > *").remove();
      },

      async resetGraph(index) {
        this.removeSvg()
        switch (index) {
          case "1":
            this.currentData = this.testData1
            break;
          case "2":
            this.currentData = this.testData2
            break;
          case "3":
            this.currentData = this.testData3
            break;
          default:
            this.$message.error('图谱信息缺省')
            break;
        }
        this.initGraph(this.currentData)
      }
    }
  }
</script>

<style scoped>
  .body {

  }

  .selectBoard {
    position: absolute;
    margin-top: 40px;
    margin-left: -42%;
  }
</style>
