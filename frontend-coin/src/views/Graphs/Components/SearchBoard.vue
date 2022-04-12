<template>
  <div class="box-card">
    <el-row class="autocomplete">
      <el-col :span="24" style="margin-top: 5px">
        <el-autocomplete
          class="inline-input"
          v-model="searchInput"
          :fetch-suggestions="querySearch"
          placeholder="查询内容"
          @select="handleSelect"
          style="width: 190px;margin-top: 8px"
          clearable
          @clear="clearSearch">

          <el-button @click="search" slot="append">
            <i class="el-icon-search"></i>
          </el-button>
        </el-autocomplete>
      </el-col>
    </el-row>
    <div>
      <el-select v-model="shapeSelectRes"
                 filterable
                 clearable
                 placeholder="过滤形状"
                 style="float: left;width: 132px;margin-top: 8px;margin-bottom: 8px;margin-right: 5px"
                 :disabled="!isShapeSelectAble"
                 :multiple=false
                 @change="selectShape">
        <el-option
          v-for="item in shapeSelect"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-switch
        v-model="isShapeSelectAble"
        active-color="#13ce66"
        inactive-color="#ff4949"
        @change="shapeSelectAbleChange"
        style="float: left ;width: 40px;margin-left: 9px;margin-bottom: 26px;margin-top: 16px">
      </el-switch>
    </div>
    <el-button icon="el-icon-close" circle
               style="margin-top: 15px;margin-bottom: 10px;background-repeat:no-repeat ;border: 0;background-color: transparent;border-style: none;"
               @click="set_searchBoardVisible(false)"></el-button>
  </div>
</template>

<script>
  import {mapActions, mapMutations, mapGetters} from 'vuex'

  export default {
    name: "SearchBoard",

    props: {
      nodes: {},
      links: {},
      rectNodes: {},
      diamondNodes: {},
      triangleUpNodes: {},
      LocalData: {},
      nodesName: {},
      linksName: {},
    },

    data() {
      return {
        searchInput: '',
        shapeItem: '',
        isShapeSelectAble: true,
        shapeSelect: [{
          label: "方形", value: 'rect'
        }, {
          label: "圆形", value: 'circle'
        }, {
          label: "菱形", value: 'diamond'
        }, {
          label: "三角形", value: ' triangle_up'
        }],
        shapeSelectRes: '',
        localTestGraph: {
          "nodes": [{
            "id": 1,
            "name": "LJ",
            "shape": "circle",
            "color": "#11FF11",
            "node_size": 50,
            "font_size": 20,
            "x": 20,
            "y": 20,
            "isShow": true,
          }, {
            "id": 2,
            "name": "CZY",
            "shape": "circle",
            "color": "#FF00FF",
            "node_size": 40,
            "font_size": 20,
            "x": 300,
            "y": 70,
            "isShow": false,
          }, {
            "id": 3,
            "name": "CZY1",
            "shape": "circle",
            "color": "#FF00FF",
            "node_size": 40,
            "font_size": 20,
            "x": 120,
            "y": 400,
            "isShow": true,
          }, {
            "id": 4,
            "name": "CZY2",
            "shape": "circle",
            "color": "#FF00FF",
            "node_size": 40,
            "font_size": 20,
            "x": 320,
            "y": 130,
            "isShow": true,
          }],
          "links": [{
            "id": 1, "source": 1, "target": 2, "name": "Friend", "isShow": true,
          },
            {
              "id": 2, "source": 3, "target": 1, "name": "Friend", "isShow": false,
            },
            {
              "id": 3, "source": 1, "target": 4, "name": "Friend", "isShow": true,
            }]
        },
        searchHistory: [],
        nodeName: [],
        timeout: null,
        selectCircleNode: '',
        selectRectNode: '',
        selectDiamondNode: '',
        selectTriangleNode: '',
        selectRel: '',

        selectLinkName: '',
        selectNodeName: '',
        selectCircleNodeName: '',
        selectDiamondNodeName: '',
        selectRectNodeName: '',
        selectTriangleNodeName: '',
      }
    },

    computed: {
      ...mapGetters([
        'customGraphRes',
        //'fuzzyMatchingOutComeList',
        'searchString',
      ])
    },

    methods: {

      ...mapMutations([
        'set_searchString',
        'set_searchBoardVisible'
      ]),

      ...mapActions([
        //'getFuzzyMatching'
      ]),

      // 组件提供方法
      // queryString 为当前的访问数据，和searchInput相同
      // cb 为 callback回调函数，展示下拉框数据
      async querySearch(queryString, cb) {
        if (this.searchInput === '') {
          cb(this.searchHistory) //历史记录，每次搜索过后，searchHistory会push一次
        } else {
          //this.set_searchString(queryString.toString())
          //await this.getFuzzyMatching()
          //let testData = [];
          //for(let i = 0; i < this.fuzzyMatchingOutComeList.length; i++){
          //  testData.push({
          //    "value":this.fuzzyMatchingOutComeList[i].name,
          //  })
          //}
          this.loadAll();
          var nodeName = this.nodeName;
          var results = queryString ? nodeName.filter(this.createStateFilter(queryString)) : nodeName;


          clearTimeout(this.timeout);
          this.timeout = setTimeout(() => {
            cb(results);
          }, 500 * Math.random());
        }
      },
      createStateFilter(queryString) {
        return (searchInput) => {
          return (searchInput.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1);
        };
      },

      // 处理选择
      async handleSelect(item) {
        //this.set_searchString(item.value.toString())
        //await this.getFuzzyMatching()
        if (this.searchInput != '') {
          this.searchInput = item.value.slice(3)
        }
        if (this.searchInput === '') {
          this.searchInput = item.value
        }

        //this.createSearchRequest()
      },
      // 查询的处理逻辑
      async search() {
        let _this = this;
        //历史记录无重复
        let tempFlag = 'false'
        for (let i = 0; i < _this.searchHistory.length; i++) {
          if (_this.searchHistory[i].value === _this.searchInput) {
            tempFlag = 'true'
            break
          }
        }
        if (tempFlag === 'false') {
          _this.searchHistory.push({value: _this.searchInput});
        }
        //this.set_searchString(this.searchInput)
        //await this.getFuzzyMatching()
        //console.log(this.fuzzyMatchingOutComeList)


        _this.selectCircleNode = _this.nodes.filter(d => d.name === _this.searchInput);
        _this.selectRectNode = _this.rectNodes.filter(d => d.name === _this.searchInput);
        _this.selectDiamondNode = _this.diamondNodes.filter(d => d.name === _this.searchInput);
        _this.selectTriangleNode = _this.triangleUpNodes.filter(d => d.name === _this.searchInput);
        _this.selectRel = _this.links.filter(d => d.name === _this.searchInput);

        _this.selectLinkName = _this.linksName.filter(d => d.name === _this.searchInput);
        _this.selectNodeName = _this.nodesName.filter(d => d.name === _this.searchInput);

        let tempLinks = this.LocalData.links


        _this.nodes.attr("opacity", 0.2);
        _this.rectNodes.attr("opacity", 0.2);
        _this.diamondNodes.attr("opacity", 0.2);
        _this.triangleUpNodes.attr("opacity", 0.2);
        _this.links.attr("opacity", 0.2);
        _this.linksName.attr("opacity", 0.2);
        _this.nodesName.attr("opacity", 0.2);

        _this.selectCircleNode.attr("opacity", 1);
        _this.selectRectNode.attr("opacity", 1);
        _this.selectDiamondNode.attr("opacity", 1);
        _this.selectTriangleNode.attr("opacity", 1);
        _this.selectRel.attr("opacity", 1);
        _this.selectLinkName.attr("opacity", 1);
        _this.selectNodeName.attr("opacity", 1);

        tempLinks.filter((item, i) => {
          if (item.name === _this.searchInput) {
            this.setOpacity(item.source.id);
            this.setOpacity(item.target.id);
          }
        })
        console.log(_this.LocalData.nodes)


        //this.createSearchRequest()
      },

      setOpacity(data) {
        console.log(data)
        let _this = this;
        _this.selectCircleNode = _this.nodes.filter(d => d.id === data);
        _this.selectRectNode = _this.rectNodes.filter(d => d.id === data);
        _this.selectDiamondNode = _this.diamondNodes.filter(d => d.id === data);
        _this.selectTriangleNode = _this.triangleUpNodes.filter(d => d.id === data);
        _this.selectLinkName = _this.linksName.filter(d => d.id === data);
        _this.selectNodeName = _this.nodesName.filter(d => d.id === data);

        _this.selectCircleNode.attr("opacity", 1);
        _this.selectRectNode.attr("opacity", 1);
        _this.selectDiamondNode.attr("opacity", 1);
        _this.selectTriangleNode.attr("opacity", 1);
        _this.selectLinkName.attr("opacity", 1);
        _this.selectNodeName.attr("opacity", 1);
      },

      //清空搜索框
      clearSearch() {
        this.searchInput = '';
        this.links.attr("opacity", 1);
        this.nodes.attr("opacity", 1);
        this.rectNodes.attr("opacity", 1);
        this.diamondNodes.attr("opacity", 1);
        this.triangleUpNodes.attr("opacity", 1);
        this.linksName.attr("opacity", 1);
        this.nodesName.attr("opacity", 1);
      },
      // 搜索完成后调用，生成图谱
      // createSearchRequest() {
      //  this.nodes.attr('opacity',0.2)
      //},
      //过滤形状
      selectShape(item) {
        this.selectCircleNodeName = this.nodesName.filter(d => d.shape === 'circle');
        this.selectDiamondNodeName = this.nodesName.filter(d => d.shape === 'diamond');
        this.selectRectNodeName = this.nodesName.filter(d => d.shape === 'rect');
        this.selectTriangleNodeName = this.nodesName.filter(d => d.shape === 'triangle_up');

        this.shapeItem = item
        let flagcircle = 'false';
        let flagrect = 'false';
        let flagdiamond = 'false';
        let flagtriangle = 'false';
        if (this.isShapeSelectAble) {
          // if(data.length>0) {
          if (item !== '') {
            this.links.attr("opacity", 0.2);
            // data.filter((item, i) => {
            //   if (item === "circle") {
            //     this.nodes.attr("opacity", 1);
            //     flagcircle = 'true';
            //   }
            //   if (item === "rect") {
            //     this.rectNodes.attr("opacity", 1);
            //     flagrect = 'true';
            //   }
            //   if (item === "diamond") {
            //     this.diamondNodes.attr("opacity", 1);
            //     flagdiamond = 'true';
            //   }
            //   if (item === " triangle_up") {
            //     this.triangleUpNodes.attr("opacity", 1);
            //     flagtriangle = 'true';
            //   }
            // });
            if (item === "circle") {
              this.nodes.attr("opacity", 1);
              this.selectCircleNodeName.attr("opacity", 1);
              flagcircle = 'true';
            }
            if (item === "rect") {
              this.rectNodes.attr("opacity", 1);
              this.selectRectNodeName.attr("opacity", 1);
              flagrect = 'true';
            }
            if (item === "diamond") {
              this.diamondNodes.attr("opacity", 1);
              this.selectDiamondNodeName.attr("opacity", 1);
              flagdiamond = 'true';
            }
            if (item === " triangle_up") {
              this.triangleUpNodes.attr("opacity", 1);
              this.selectTriangleNodeName.attr("opacity", 1);
              flagtriangle = 'true';
            }
            if (flagcircle === 'false') {
              this.linksName.attr("opacity", 0.2);
              this.nodes.attr("opacity", 0.2);
              this.selectCircleNodeName.attr("opacity", 0.2);
            }
            if (flagrect === 'false') {
              this.linksName.attr("opacity", 0.2);
              this.rectNodes.attr("opacity", 0.2);
              this.selectRectNodeName.attr("opacity", 0.2);
            }
            if (flagdiamond === 'false') {
              this.linksName.attr("opacity", 0.2);
              this.diamondNodes.attr("opacity", 0.2);
              this.selectDiamondNodeName.attr("opacity", 0.2);
            }
            if (flagtriangle === 'false') {
              this.linksName.attr("opacity", 0.2);
              this.triangleUpNodes.attr("opacity", 0.2);
              this.selectTriangleNodeName.attr("opacity", 0.2);
            }
          } else {
            this.links.attr("opacity", 1);
            this.nodes.attr("opacity", 1);
            this.rectNodes.attr("opacity", 1);
            this.diamondNodes.attr("opacity", 1);
            this.triangleUpNodes.attr("opacity", 1);
            this.linksName.attr("opacity", 1);
            this.selectCircleNodeName.attr("opacity", 1);
            this.selectRectNodeName.attr("opacity", 1);
            this.selectDiamondNodeName.attr("opacity", 1);
            this.selectTriangleNodeName.attr("opacity", 1);
          }
        }
      },
      shapeSelectAbleChange(data) {
        if (!data) {
          this.links.attr("opacity", 1);
          this.nodes.attr("opacity", 1);
          this.rectNodes.attr("opacity", 1);
          this.diamondNodes.attr("opacity", 1);
          this.triangleUpNodes.attr("opacity", 1);
          this.linksName.attr("opacity", 1);
          this.selectCircleNodeName.attr("opacity", 1);
          this.selectRectNodeName.attr("opacity", 1);
          this.selectDiamondNodeName.attr("opacity", 1);
          this.selectTriangleNodeName.attr("opacity", 1);
        } else {
          this.selectShape(this.shapeItem)
        }
      },

      loadAll() {
        let filterData = [];
        this.LocalData.nodes.filter((item, i) => {
          let flag = 'false';
          filterData.filter((item1, i1) => {
            if (item1.value === "关系:" + item.name) {
              flag = 'true';
            }
          });
          if (flag === 'false') {
            filterData.push({value: "节点:" + item.name})
          }
        });
        this.LocalData.links.filter((item, i) => {
          let flag = 'false';
          filterData.filter((item1, i1) => {
            if (item1.value === "关系:" + item.name) {
              flag = 'true';
            }
          });
          if (flag === 'false') {
            filterData.push({value: "关系:" + item.name})
          }
        });
        this.nodeName = filterData;
      }
    },
  }
</script>

<style scoped>
  .box-card {
    margin-top: 10px;
    margin-right: 10px;
    padding-left: 10px;
    padding-right: 10px;
    position: absolute;
    width: 190px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    border-radius: 4px;
    text-align: center;
  }
</style>
