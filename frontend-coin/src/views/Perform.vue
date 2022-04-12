<template>
  <el-container style="height:100%;" direction="vertical">
    <el-header>
      <div style="margin-left:40%">
        <div style="float:left;font-weight: 700;font-size:18px">知识图谱可视化
        </div>
        <div style="float:left;margin-top: 2px">

          <el-dropdown trigger="click" @command="change_modal">
            <span class="show_model_change-link" style="font-weight: 700;font-size:15px">
        （{{this.showModel}}）<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="力导图模式">力导图模式</el-dropdown-item>
              <el-dropdown-item command="排版模式">排版模式</el-dropdown-item>
              <el-dropdown-item command="数值统计">数值统计</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </el-header>
    <el-container>
      <el-main>
        <div v-show="this.showModel==='力导图模式'">
          <div class="box">

            <div class="context-menu">
              <ul class="opt">
                <li class="opt_add">
                  <a class="opt_1" href="javascript:void(0);" @click="AddObjectVisible=true">添加对象</a>
                </li>
                <li class="opt_add">
                  <a class="opt_1" href="javascript:void(0);" @click="flagAddRelation='true'">添加关系</a>
                </li>
              </ul>
            </div>

            <!--            <el-button style="float: left; margin-left: 10px;margin-top: 10px;margin-bottom: 10px"-->
            <!--                       @click="AddObjectVisible=true">添加对象-->
            <!--            </el-button>-->

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

            <!--            <el-button style="float: left; margin-left: 10px;margin-top: 10px" @click="flagAddRelation='true'">添加关系</el-button>-->

            <!-- 创建关系-->
            <el-dialog
              title="创建新关系"
              :visible.sync="AddRelationVisible"
              width="30%">
            <span>
              <el-input v-model="input_Relation" placeholder="关系名"></el-input>
            </span>
              <el-select v-model="isSolid" placeholder="实线"
                         style="float:left;width: 47% ;margin-top: 10px;margin-bottom: 10px">
                <el-option
                  v-for="item in lineModal"
                  :key="item.isSolid"
                  :label="item.label"
                  :value="item.isSolid">
                </el-option>
              </el-select>

              <el-select v-model="isShow" placeholder="显示关系名"
                         style="float:left;width: 47%;margin-top: 10px;margin-bottom: 10px;margin-left: 6%">
                <el-option
                  v-for="item in lineName"
                  :key="item.isShow"
                  :label="item.label"
                  :value="item.isShow">
                </el-option>
              </el-select>
              <span slot="footer" class="dialog-footer">
            <el-button @click="CancelAddRelation">取 消</el-button>
            <el-button type="primary" @click="handleAddRelationClose">确 定</el-button>
            </span>
            </el-dialog>

            <el-button style="float: right; margin-right: 10px;margin-top: 10px; margin-bottom: 10px"
                       @click="confirm_deleteAllVisible=true">清空对象
            </el-button>
            <el-dialog title="清空对象" :visible.sync="confirm_deleteAllVisible" width="30%">
              <el-button @click="confirm_deleteAllVisible=false">取消</el-button>
              <el-button @click="handleDeleteAll" type="danger">确认</el-button>
            </el-dialog>

            <NodeClick :getAllData="getAllData" :removeSvg="removeSvg" :initGraph="initGraph"
                       v-show="nodeClickVisible"/>
            <RelationClick :getAllData="getAllData" :removeSvg="removeSvg" :initGraph="initGraph"
                           v-show="relationClickVisible"/>

            <div class="container" id="pic">
              <div style="width: 200px;margin-top:60px;margin-left: 10px">
              </div>
            </div>

          </div>
        </div>
        <div v-show="this.showModel==='数值统计'">
          <div class="box">
            <DataModal v-bind:tableData_temp="customGraphRes.nodes" ref="DataModalRef"></DataModal>
          </div>
        </div>
        <div v-show="this.showModel==='排版模式'">
          <div class="box">
            <TypesettingModal class="typeset" ref="TypesettingModal"></TypesettingModal>
          </div>
        </div>
      </el-main>

      <!--操作平台-->
      <el-aside class="operatePlatform" width="220px" v-show="this.showModel==='力导图模式'">
        <search-board v-bind:nodes="nodes" v-bind:rectNodes="rectNodes" v-bind:triangleUpNodes="triangleUpNodes"
                      v-bind:diamondNodes="diamondNodes" v-bind:links="links" v-bind:LocalData="customGraphRes"
                      v-bind:linksName="linksName" v-bind:nodesName="nodesName"></search-board>
        <!--        <div style="top:5px">-->
        <!--          <el-button id="zoomOut" icon="el-icon-zoom-in"></el-button>-->
        <!--          <el-button id="translateY_UP"  icon="el-icon-top"></el-button>-->
        <!--          <el-button id="zoomIn"  icon="el-icon-zoom-out"></el-button>-->
        <!--        </div>-->
        <div>
          <!--          <el-button id="translateX_LEFT"  icon="el-icon-back"></el-button>-->
          <!--          <el-button id="translateY_DOWN"  icon="el-icon-bottom"></el-button>-->
          <!--          <el-button id="translateX_RIGHT"  icon="el-icon-right"></el-button>-->
          <el-button id="reset" icon="el-icon-refresh-left"></el-button>
          <el-button id="refresh" icon="el-icon-refresh" @click="graphUpdate"></el-button>
        </div>

        <Aside :getAllData="getAllData" :removeSvg="removeSvg" :initGraph="initGraph"></Aside>
      </el-aside>

    </el-container>
  </el-container>
</template>

<script>
  import {mapMutations, mapActions, mapGetters} from 'vuex'
  import * as d3 from 'd3'
  import Aside from './Aside'
  import DataModal from './DataModal'
  import TypesettingModal from './TypesettingModal'
  import NodeClick from './Graphs/Components/NodeClick'
  import SearchBoard from "./Graphs/Components/SearchBoard"
  import RelationClick from "./Graphs/Components/RelationClick";

  export default {
    name: 'Perform',
    components: {
      RelationClick,
      Aside,
      NodeClick,
      DataModal,
      SearchBoard,
      TypesettingModal,
    },
    data() {
      return {
        //添加新节点
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

        //添加关系
        flagAddRelation: 'false',
        mousedownNode: '',
        mouseupNode: '',
        moveStop: '',
        DrawLine: '',
        startX: '',
        startY: '',
        Xstart: '',
        Ystart: '',
        zoomOperate: [],
        AddRelationVisible: false,
        input_Relation: '',
        //实虚线，是否显示名字
        lineModal: [{
          isSolid: 'true',
          label: '实线'
        }, {
          isSolid: 'false',
          label: '虚线'
        }],
        isSolid: 'true',

        lineName: [{
          isShow: 'true',
          label: '显示关系名'
        }, {
          isShow: 'false',
          label: '不显示关系名'
        }],
        isShow: 'true',

        // 清空图谱
        confirm_deleteAllVisible: false,

        time: 1,
        tempwidth: 625,
        tempheight: 265,
        startXY: [],

        svgArea: null,
        width: 1250,
        height: 520,
        links: [],
        nodes: [],
        rectNodes: [], //方形节点
        triangleUpNodes: [], //三角形节点
        diamondNodes: [], //菱形节点
        nodesName: [],
        linksName: [],
        simulation: null,
        localTestGraph: {
          "nodes": [{
            "id": 1,
            "name": "LJ",
            "shape": "triangle_up",
            "color": "#11FF11",
            "node_size": 50,
            "font_size": 20,
            "x": 20,
            "y": 20,
            "isShown": true,
          }, {
            "id": 3,
            "name": "CZY1",
            "shape": "circle",
            "color": "#FF00FF",
            "node_size": 40,
            "font_size": 20,
            "x": 120,
            "y": 400,
            "isShown": true,
          }, {
            "id": 4,
            "name": "CZY3",
            "shape": "circle",
            "color": "#FF00FF",
            "node_size": 40,
            "font_size": 20,
            "x": 320,
            "y": 130,
            "isShown": true,
          }, {
            "id": 5,
            "name": "CZY4",
            "shape": "rect",
            "color": "#FF00FF",
            "node_size": 40,
            "font_size": 20,
            "x": 320,
            "y": 130,
            "isShown": true,
          }],
          "links": [{
            "id": 1, "source": 1, "target": 5, "name": "Friend", "isShown": true, "isSolid": false,
          },
            {
              "id": 2, "source": 3, "target": 1, "name": "Friend", "isShown": false, "isSolid": true,
            },
            {
              "id": 3, "source": 1, "target": 4, "name": "Friend", "isShown": true, "isSolid": true,
            }]
        },
      }
    },
    computed: {
      ...mapGetters([
        'customNodeParams',
        'customRelationParams',
        'currentNodeId',
        'customGraphRes',
        'customGraphParams',
        'currentGraphId',
        'showModel',
        'graphList',
        'nodeClickVisible',
        'relationClickVisible',
      ])
    },

    // 初始化默认为力导图模式，获取所有图谱信息，默认初始图谱为图谱列表第一个并生成图谱
    async mounted() {
      this.RightClick();
      this.set_showModel("力导图模式")
      await this.getGraphLists()
      this.set_currentGraphId(this.graphList[0].id)
      this.set_currentGraphName(this.graphList[0].name)
      this.set_customGraphParams(this.graphList[0])
      await this.getAllData()
      await this.initGraph(this.customGraphRes)
      // this.initGraph(this.localTestGraph)
    },

    methods: {
      ...mapMutations([
        'set_customNodeParams',
        'set_customRelationParams',
        'set_customGraphParams',
        'set_showModel',
        'set_nodeClickVisible',
        'set_relationClickVisible',
        'set_currentGraphId',
        'set_currentGraphName',
        'set_customNodeParamsClear',
      ]),

      ...mapActions([
        'addCustomNode',
        'addCustomRelation',
        'getAllGraph',
        'getCustomGraph',
        'getTypeSetting',
        'addGraph',
        'deleteAllNodesInGraph',
        'editCustomNode',
      ]),

      // 获取所有图谱基本信息；如果数据库中没有图谱。新增名为new graph的图谱并重新获取
      async getGraphLists() {
        await this.getAllGraph();
        if (this.graphList.length === 0) {
          this.set_customGraphParams({
            name: 'new',
            isNodeLabelVisible: true,
            isRelationLabelVisible: true,
          })
          await this.addGraph();
          await this.getAllGraph();
        }
      },

      async graphUpdate() {
        console.log(this.customGraphRes.nodes)
        for (let i = 0; i < this.customGraphRes.nodes.length; i++) {
          this.set_customNodeParams(this.customGraphRes.nodes[i])
          await this.editCustomNode();
        }
      },

      async getAllData() {
        console.log('getAllData is get')
        if (this.showModel === '力导图模式') {
          await this.getCustomGraph()
        } else if (this.showModel === '排版模式') {
          await this.getTypeSetting()
        }
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
        console.log(this.customNodeParams)
        await this.addCustomNode()
        await this.getAllData()
        await this.removeSvg()
        await this.initGraph(this.customGraphRes)

        // 关闭视图
        this.AddObjectVisible = false

        //清空
        this.input_Object = '';
        this.ShapeValue = '';
        this.input_NodeSize = '';
        this.input_NodeColor = '';
        this.input_FontSize = '';
      },

      // 添加关系
      async handleAddRelationClose() {
        const data = {
          source: this.mousedownNode.id,
          target: this.mouseupNode.id,
          graphId: this.currentGraphId,
          name: this.input_Relation,
          shown: this.isShow,
          solid: this.isSolid,
          isHighlighted: false,
        }
        this.flagAddRelation = 'false';
        await this.set_customRelationParams(data)
        await this.addCustomRelation()
        await this.getAllData()
        await this.removeSvg()
        await this.initGraph(this.customGraphRes)

        this.zoomOperate = [];

        this.AddRelationVisible = false;
        // 清空
        this.mouseupNode = ''
        this.mousedownNode = ''
        this.input_Relation = ''
      },

      // 清空当前图谱
      async handleDeleteAll() {
        await this.deleteAllNodesInGraph()
        await this.getAllData()
        await this.removeSvg()
        this.confirm_deleteAllVisible = false
        await this.initGraph(this.customGraphRes)
      },

      initGraph(data) {
        console.log('init!')
        const links = data.links
        const nodes = data.nodes
        var _this = this;
        _this.time = 1;
        _this.zoomOperate = [];

        this.simulation = d3.forceSimulation(nodes)
          .force("link", d3.forceLink(links).id(d => d.id).distance(200).strength(0))
          .force("collide", d3.forceCollide().radius(() => 50))
          .force("charge", d3.forceManyBody().strength(-40))
          .force("center", d3.forceCenter(this.width / 2, this.height / 2));

        let zoom = d3.zoom()
          .filter(() => this.flagAddRelation === 'false')
          .scaleExtent([0.25, 4])
          .translateExtent([[-1000, -500], [this.width + 1000, this.height + 500]])
          .on("zoom", function (event) {
            g.attr("transform", event.transform)
          })

        d3.select(".box")
          .attr("preserveAspectRatio", "xMinYMin meet")
          .call(zoom);


        this.svgArea = d3.select(".container")
          .append("svg")
          .attr("viewBox", [0, 0, this.width, this.height])
          .attr("class", "svgCanvas")

        d3.select("#reset")
          .on("click", function (event) {
            d3.select(".box")
              .attr("preserveAspectRatio", "xMinYMin meet")
              .transition().duration(1000).call(zoom.transform, d3.zoomIdentity);
            _this.zoomOperate = [];
          })


        d3.select("#zoomIn")
          .on("click", function () {
            zoom.scaleBy(g, 0.5);
            _this.time = _this.time * 0.5;
            _this.zoomOperate.push("In");

          })

        d3.select("#zoomOut")
          .on("click", function () {
            if (_this.time < 4) {
              zoom.scaleBy(g, 2);
              _this.time = _this.time * 2;
              _this.zoomOperate.push("Out");
            }
          })

        d3.select("#translateX_LEFT")
          .on("click", function (d) {
            zoom.translateBy(g, -20, 0);
            _this.zoomOperate.push("Left");
          });

        d3.select("#translateX_RIGHT")
          .on("click", function (d) {
            zoom.translateBy(g, 20, 0);
            _this.zoomOperate.push("Right");
          });

        d3.select("#translateY_UP")
          .on("click", function (d) {
            zoom.translateBy(g, 0, -20);
            _this.zoomOperate.push("Up");
          })

        d3.select("#translateY_DOWN")
          .on("click", function (d) {
            zoom.translateBy(g, 0, 20);
            _this.zoomOperate.push("Down");
          })

        this.addMarkers();

        const g = this.svgArea.append("g")
          .attr("class", "content")
        // .call(zoom)

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
          .attr("stroke", "rgba(45,64,111,0.38)")
          .attr("stroke-opacity", 0.8)
          .attr("stroke-dasharray", function (d) {
            if (d.solid) {
              return "1,0"
            } else {
              return "5,5"
            }
          })
          .attr("marker-end", "url(resolved)")
          .attr("fill-opacity", 0)
          .attr("stroke-width", 2)
          .attr("class", "link")
          .on("click", this.linkClick)
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
          .style('fill', 'rgba(45,64,111,0.5)')
          .attr('xlink:href', function (d) {
            if (typeof (d.source) === 'object') {
              return "#" + d.source.id + "_" + d.name + "_" + d.target.id
            } else {
              return "#" + d.source + "_" + d.name + "_" + d.target
            }
          })
          .attr('startOffset', '50%')
          .text(d => d.name)
          .style("visibility", function (d) {
            if (_this.customGraphParams.isRelationLabelVisible && d.shown) {
              return "visible"
            } else {
              return "hidden"
            }
          });


        this.DrawLine = this.svgArea.append("path")
          .attr("stroke", "rgba(45,64,111,0.38)")
          .attr("stroke-opacity", 0.8)
          .attr("marker-end", "url(resolved)")
          .attr("fill-opacity", 0)
          .attr("stroke-width", 3)
          .attr("marker-end", function (d) {
            return "url(#positiveMarker)"
          });


        this.svgArea
          .on('mousedown', (event, d) => {
              if (this.mousedownNode === '' || this.flagAddRelation === 'false') return;
              this.startXY = d3.pointer(event);
            }
          )
          .on('mouseup', (event, d) => {
            this.moveStop = 'true';
            console.log(this.mouseupNode.index);
            console.log(this.mousedownNode.index);
            if (this.mouseupNode === '' || this.mouseupNode.index === this.mousedownNode.index) {
              this.DrawLine.attr("stroke-width", 0).attr("marker-end", "url(resolved)").attr("marker-end", function (d) {
                return;
              }).attr("d", function (d) {
                return;
              });
            } else {
              if (this.flagAddRelation === 'false') return;
              this.AddRelationVisible = true;
            }
          })
          .on('mousemove', (event, d) => {
            let that = this;
            if (this.mousedownNode === '' || !this.mouseupNode === '' || this.moveStop) return;
            let startX = that.mousedownNode.x;
            let startY = that.mousedownNode.y;
            //   na- width / 2 *( n - 1)，nb- height/ 2 *( n - 1)
            //1250 620
            // this.zoomOperate.filter( (item,i) =>{
            //   if(item==="Left"){
            //     startX=startX-20*that.time;
            //     transX=transX-10*that.time;
            //   }
            //   if(item==="Right"){
            //     startX=startX+20*that.time;
            //     transX=transX+10*that.time;
            //   }
            //   if(item==="Up"){
            //     startY=startY-20*that.time;
            //     transY=transY-10*that.time;
            //   }
            //   if(item==="Down"){
            //     startY=startY+20*that.time;
            //     transY=transY+10*that.time;
            //   }
            //   if(item==="In"){
            //     if (that.time <= 4) {
            //       Width=Width-transX;
            //       Height=Height-transY;
            //       startX = 0.5 * startX - this.tempwidth  * (0.5 - 1);
            //       startY = 0.5 * startY - this.tempheight  * (0.5 - 1)
            //     }
            //   }
            //   if(item==="Out"){
            //     if (that.time >=0.125) {
            //       Width=Width-transX;
            //       Height=Height-transY;
            //       startX = 2 * startX - this.tempwidth* (2 - 1);
            //       startY = 2 * startY - this.tempheight* (2 - 1)
            //     }
            //   }
            // } )
            startX = that.startXY[0];
            startY = that.startXY[1];
            this.Xstart = startX;
            this.Ystart = startY;
            let [x, y] = d3.pointer(event)
            that.DrawLine
              .attr("d", function (d) {
                if (startX <= x && startY <= y) {
                  return "M " + (startX) + " " + (startY) + " L " + (x - 2) + " " + (y - 2)
                } else if (startX <= x && startY > y) {
                  return "M " + (startX) + " " + (startY) + " L " + (x - 2) + " " + (y + 2)
                } else if (startX > x && startY <= y) {
                  return "M " + (startX) + " " + (startY) + " L " + (x + 2) + " " + (y - 2)
                } else {
                  return "M " + (startX) + " " + (startY) + " L " + (x + 2) + " " + (y + 2)
                }
              });
          })

        this.nodes = g.append("g")
          .attr("stroke", "rgba(45,64,111,0.5)")
          .attr("stroke-width", 1)
          .selectAll("circle")
          .data(nodes)
          .join("circle")
          .filter(d => d.shape === "circle")
          .attr("r", d => d.node_size)
          .attr("class", "node")
          .attr("fill", d => d.color)
          .on("click", this.nodeClick)
          .call(this.drag(this.simulation))
          .on('mousedown', (event, d) => {
            event.preventDefault();
            this.mousedownNode = d;
            this.mouseupNode = '';
            this.DrawLine.attr("stroke-width", 3).attr("marker-end", "url(resolved)").attr("marker-end", function (d) {
              return "url(#positiveMarker)";
            })
            //console.log(this.mousedownNode.x+":"+this.mousedownNode.y)
            this.moveStop = '';
          })
          .on('mouseup', (event, d) => {
            this.mouseupNode = d;
          })
          .on('mouseover', function (d) {
            this.mouseupNode = d;
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

        //方形、三角形、菱形节点初始化
        this.rectNodes = g.append("g")
          .attr("stroke", "rgba(45,64,111,0.5)")
          .attr("stroke-width", 1)
          .selectAll("rect")
          .data(nodes)
          .enter()
          .filter(d => d.shape === "rect")
          .append("rect")
          .attr("width", d => d.node_size * 1.41) //方形为对应size原型的内切方形
          .attr("height", d => d.node_size * 1.41)
          .attr("class", "node")
          .attr("fill", d => d.color)
          .on("click", this.nodeClick)
          .call(this.drag(this.simulation))
          .on('mousedown', (event, d) => {
            event.preventDefault();
            this.DrawLine.attr("stroke-width", 3).attr("marker-end", "url(resolved)").attr("marker-end", function (d) {
              return "url(#positiveMarker)";
            })
            this.mousedownNode = d;
            this.mouseupNode = '';
            this.moveStop = '';
          })
          .on('mouseup', (event, d) => {
            this.mouseupNode = d;
          })
          .on('mouseover', function (d) {
            this.mouseupNode = d;
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

        this.triangleUpNodes = g.append("g")
          .attr("stroke", "rgba(45,64,111,0.5)")
          .attr("stroke-width", 1)
          .selectAll("triangle_up")
          .data(nodes)
          .enter()
          .filter(d => d.shape === "triangle_up")
          .append('path')
          .attr("class", "node")
          .attr("fill", d => d.color)
          .on("click", this.nodeClick)
          .call(this.drag(this.simulation))
          .on('mousedown', (event, d) => {
            event.preventDefault();
            this.DrawLine.attr("stroke-width", 3).attr("marker-end", "url(resolved)").attr("marker-end", function (d) {
              return "url(#positiveMarker)";
            })
            this.mousedownNode = d;
            this.mouseupNode = '';
            this.moveStop = '';
          })
          .on('mouseup', (event, d) => {
            this.mouseupNode = d;
          })
          .on('mouseover', function (d) {
            this.mouseupNode = d;
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

        this.diamondNodes = g.append("g")
          .attr("stroke", "rgba(45,64,111,0.5)")
          .attr("stroke-width", 1)
          .selectAll("diamond")
          .data(nodes)
          .enter()
          .filter(d => d.shape === "diamond")
          .append('path')
          .attr("class", "node")
          .attr("fill", d => d.color)
          .on("click", this.nodeClick)
          .call(this.drag(this.simulation))
          .on('mousedown', (event, d) => {
            event.preventDefault();
            this.DrawLine.attr("stroke-width", 3).attr("marker-end", "url(resolved)").attr("marker-end", function (d) {
              return "url(#positiveMarker)";
            })
            this.mousedownNode = d;
            this.mouseupNode = '';
            this.moveStop = '';
          })
          .on('mouseup', (event, d) => {
            this.mouseupNode = d;
          })
          .on('mouseover', function (d) {
            this.mouseupNode = d;
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

        this.nodesName = g.append("g")
          .selectAll("text")
          .data(nodes)
          .join("text")
          .text(d => d.name)
          .attr("dx", function (d) {
            return (d.node_size / 2 + d.font_size) / 2 * (-1)
          })
          .attr("dy", function (d) {
            return d.node_size + d.font_size - 5
          })
          .style("font-size", function (d) {
            return d.font_size
          })
          .attr("class", "nodeName")
          .attr("fill", d => d.color)
          .style("visibility", function (d) {
            if (d.shown && _this.customGraphParams.isNodeLabelVisible) {
              return "visible"
            } else {
              return "hidden"
            }
          });

        //(d.target.node_size)/(Math.sqrt(Math.pow(d.target.y-d.source.y,2)+Math.pow(d.target.x-d.source.x,2)))

        this.simulation.on("tick", () => {
          this.links
            .attr("d", function (d) {
              if ((d.source.shape !== 'circle') && (d.target.shape !== 'circle')) {
                if (d.source.x < d.target.x && d.source.y < d.target.y) {
                  return ("M " +
                    (d.source.x - (d.source.x - d.target.x) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " " +
                    (d.source.y - (d.source.y - d.target.y) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " L " +
                    (d.target.x - (d.target.x - d.source.x) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " " +
                    (d.target.y - (d.target.y - d.source.y) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                  )
                } else if (d.source.x < d.target.x && d.source.y >= d.target.y) {
                  return ("M " +
                    (d.source.x - (d.source.x - d.target.x) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " " +
                    (d.source.y - (d.source.y - d.target.y) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " L " +
                    (d.target.x - (d.target.x - d.source.x) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " " +
                    (d.target.y - (d.target.y - d.source.y) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                  )
                } else if (d.source.x > d.target.x && d.source.y < d.target.y) {
                  return ("M " +
                    (d.target.x - (d.target.x - d.source.x) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " " +
                    (d.target.y - (d.target.y - d.source.y) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " L " +
                    (d.source.x - (d.source.x - d.target.x) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " " +
                    (d.source.y - (d.source.y - d.target.y) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                  )
                } else {
                  return ("M " +
                    (d.target.x - (d.target.x - d.source.x) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " " +
                    (d.target.y - (d.target.y - d.source.y) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " L " +
                    (d.source.x - (d.source.x - d.target.x) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " " +
                    (d.source.y - (d.source.y - d.target.y) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                  )
                }
              }
              if ((d.source.shape !== 'circle') && (d.target.shape === 'circle')) {
                if (d.source.x < d.target.x && d.source.y < d.target.y) {
                  return ("M " +
                    (d.source.x - (d.source.x - d.target.x) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " " +
                    (d.source.y - (d.source.y - d.target.y) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " L " +
                    (d.target.x - (d.target.x - d.source.x) * (d.target.node_size * 1.2) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " " +
                    (d.target.y - (d.target.y - d.source.y) * (d.target.node_size * 1.2) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                  )
                } else if (d.source.x < d.target.x && d.source.y >= d.target.y) {
                  return ("M " +
                    (d.source.x - (d.source.x - d.target.x) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " " +
                    (d.source.y - (d.source.y - d.target.y) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " L " +
                    (d.target.x - (d.target.x - d.source.x) * (d.target.node_size * 1.2) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " " +
                    (d.target.y - (d.target.y - d.source.y) * (d.target.node_size * 1.2) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                  )
                } else if (d.source.x > d.target.x && d.source.y < d.target.y) {
                  return ("M " +
                    (d.target.x - (d.target.x - d.source.x) * (d.target.node_size * 1.2) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " " +
                    (d.target.y - (d.target.y - d.source.y) * (d.target.node_size * 1.2) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " L " +
                    (d.source.x - (d.source.x - d.target.x) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " " +
                    (d.source.y - (d.source.y - d.target.y) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                  )
                } else {
                  return ("M " +
                    (d.target.x - (d.target.x - d.source.x) * (d.target.node_size * 1.2) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " " +
                    (d.target.y - (d.target.y - d.source.y) * (d.target.node_size * 1.2) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " L " +
                    (d.source.x - (d.source.x - d.target.x) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " " +
                    (d.source.y - (d.source.y - d.target.y) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                  )
                }
              }
              if ((d.source.shape === 'circle') && (d.target.shape !== 'circle')) {
                if (d.source.x < d.target.x && d.source.y < d.target.y) {
                  return ("M " +
                    (d.source.x - (d.source.x - d.target.x) * (d.source.node_size * 1.2) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " " +
                    (d.source.y - (d.source.y - d.target.y) * (d.source.node_size * 1.2) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " L " +
                    (d.target.x - (d.target.x - d.source.x) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " " +
                    (d.target.y - (d.target.y - d.source.y) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                  )
                } else if (d.source.x < d.target.x && d.source.y >= d.target.y) {
                  return ("M " +
                    (d.source.x - (d.source.x - d.target.x) * (d.source.node_size * 1.2) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " " +
                    (d.source.y - (d.source.y - d.target.y) * (d.source.node_size * 1.2) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " L " +
                    (d.target.x - (d.target.x - d.source.x) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " " +
                    (d.target.y - (d.target.y - d.source.y) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                  )
                } else if (d.source.x > d.target.x && d.source.y < d.target.y) {
                  return ("M " +
                    (d.target.x - (d.target.x - d.source.x) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " " +
                    (d.target.y - (d.target.y - d.source.y) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " L " +
                    (d.source.x - (d.source.x - d.target.x) * (d.source.node_size * 1.2) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " " +
                    (d.source.y - (d.source.y - d.target.y) * (d.source.node_size * 1.2) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                  )
                } else {
                  return ("M " +
                    (d.target.x - (d.target.x - d.source.x) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " " +
                    (d.target.y - (d.target.y - d.source.y) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.y - d.source.y, 2) + Math.pow(d.target.x - d.source.x, 2))))
                    + " L " +
                    (d.source.x - (d.source.x - d.target.x) * (d.source.node_size * 1.2) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                    + " " +
                    (d.source.y - (d.source.y - d.target.y) * (d.source.node_size * 1.2) / (Math.sqrt(Math.pow(d.source.y - d.target.y, 2) + Math.pow(d.source.x - d.target.x, 2))))
                  )
                }
              }

              //else if(d.source.x === d.target.x){
              //   return "M" + (d.target.x + 20) + " " + (d.target.y - 10) + " C " + (d.target.x + 120) + " " + (d.target.y - 60)
              //     + " " + (d.target.x + 120) + " " + (d.target.y + 60) + "," + (d.target.x + 20) + " " + (d.target.y+ 10);
              //}
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

          this.rectNodes
            .attr("x", d => d.x - d.node_size * 0.707) //方形中心点
            .attr("y", d => d.y - d.node_size * 0.707);

          this.diamondNodes
            .attr("d", function (d) {
              return "M " + (d.x - d.node_size) + " " + (d.y) + " L " + (d.x + d.node_size - d.node_size) + " " + (d.y + d.node_size) + " L " + (d.x + 2 * d.node_size - d.node_size) + " " + (d.y) + " L " + (d.x + d.node_size - d.node_size) + " " + (d.y - d.node_size) + " L " + (d.x - d.node_size) + " " + (d.y)
            });

          this.triangleUpNodes
            .attr("d", function (d) {
              return "M " + (d.x - 0.87 * d.node_size) + " " + (d.y + 0.5 * d.node_size) + " L " + (d.x + 0.87 * d.node_size - 0.87 * d.node_size) + " " + (d.y - 1.5 * d.node_size + 0.5 * d.node_size) + " L " + (d.x + d.node_size * 1.73 - 0.87 * d.node_size) + " " + (d.y + 0.5 * d.node_size) + " L " + (d.x - 0.87 * d.node_size) + " " + (d.y + 0.5 * d.node_size)
            });

          this.nodesName
            .filter(d => d.shape === "diamond")
            .attr("x", function (d) {
              if (d.name.length > 1) {
                return d.x + 0.5 * d.node_size - this.getComputedTextLength() * 0.4
              } else {
                return d.x + 0.5 * d.node_size
              }
            })
            .attr("y", d => d.y + 5);

          this.nodesName
            .filter(d => d.shape === "circle")
            .attr("x", function (d) {
              if (d.name.length > 1) {
                return d.x + 0.5 * d.node_size - this.getComputedTextLength() * 0.45
              } else {
                return d.x + 0.40 * d.node_size
              }
            })
            .attr("y", d => d.y + 5);

          this.nodesName
            .filter(d => d.shape === "rect")
            .attr("x", function (d) {
              if (d.name.length > 1) {
                return d.x + 0.5 * d.node_size - this.getComputedTextLength() * 0.4
              } else {
                return d.x + 0.5 * d.node_size
              }
            })
            .attr("y", d => d.y);

          this.nodesName
            .filter(d => d.shape === "triangle_up")
            .attr("x", function (d) {
              if (d.name.length > 1) {
                return d.x + 0.5 * d.node_size - this.getComputedTextLength() * 0.4
              } else {
                return d.x + 0.5 * d.node_size
              }
            })
            .attr("y", d => d.y - 4);
        });
      },

      removeSvg() {
        console.log('remove!')
        d3.selectAll(".container > *").remove();
      },


      addMarkers() {
        const positiveMarker = this.svgArea.append("marker")
          .attr("id", "positiveMarker")
          .attr("orient", "auto")
          .attr("stroke-width", 2)
          .attr("markerUnits", "strokeWidth")
          .attr("markerUnits", "userSpaceOnUse")
          .attr("viewBox", "0 -5 10 10")
          .attr("refX", 11)
          .attr("refY", 0)
          .attr("markerWidth", 12)
          .attr("markerHeight", 12)
          .append("path")
          .attr("d", "M 0 -5 L 10 0 L 0 5")
          .attr('fill', 'rgba(45,64,111,0.5)')
          .attr("stroke-opacity", 0.6);
        const negativeMarker = this.svgArea.append("marker")
          .attr("id", "negativeMarker")
          .attr("orient", "auto")
          .attr("stroke-width", 2)
          .attr("markerUnits", "strokeWidth")
          .attr("markerUnits", "userSpaceOnUse")
          .attr("viewBox", "0 -5 10 10")
          .attr("refX", 0)
          .attr("refY", 0)
          .attr("markerWidth", 12)
          .attr("markerHeight", 12)
          .append("path")
          .attr("d", "M 10 -5 L 0 0 L 10 5")
          .attr('fill', 'rgba(45,64,111,0.5)')
          .attr("stroke-opacity", 0.6);
      },

      drag(simulation) {
        let that = this

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
          let dragNode = that.customGraphRes.nodes.find((n) => n.id === event.subject.id)
          dragNode.x = event.subject.x;
          dragNode.y = event.subject.y;
        }

        return d3.drag()
          .filter(() => this.flagAddRelation === 'false')
          .on("start", dragstarted)
          .on("drag", dragged)
          .on("end", dragended);

      },

      CancelAddRelation() {
        this.AddRelationVisible = false;
        this.mouseupNode = '';
        this.DrawLine.attr("stroke-width", 0).attr("marker-end", "url(resolved)").attr("marker-end", function (d) {
          return;
        }).attr("d", function (d) {
          return;
        });
        this.flagAddRelation = 'false';
      },

      nodeClick(d, i) {
        if (this.flagAddRelation === 'false') {
          this.set_nodeClickVisible(true);
          this.set_relationClickVisible(false);
          this.set_customNodeParams(i);
          console.log(this.nodeClickVisible)
          console.log(this.customNodeParams)
        }
      },

      linkClick(d, i) {
        this.set_relationClickVisible(true);
        this.set_nodeClickVisible(false);
        this.set_customRelationParams(i);
        console.log(this.relationClickVisible)
        console.log(this.customRelationParams)
      },

      async change_modal(command) {
        if (command === '排版模式') {
          this.removeSvg();
          this.$refs.DataModalRef.parentRemove();
          this.$refs.TypesettingModal.removeSvg();
          this.$refs.TypesettingModal.getTypeSettingData();
        }
        if (command === '力导图模式') {
          this.removeSvg();
          this.$refs.DataModalRef.parentRemove();
          this.$refs.TypesettingModal.removeSvg();
          this.getAllData();
          this.initGraph(this.customGraphRes)
        }
        if (command === '数值统计') {
          //console.log(this.customGraphRes.nodes);
          this.removeSvg();
          this.$refs.DataModalRef.parentRemove();
          this.$refs.DataModalRef.set(this.customGraphRes.nodes);
          this.$refs.DataModalRef.dataset(this.customGraphRes.nodes);
          this.$refs.DataModalRef.parentDrawPie();
        }
        this.set_showModel(command)
      },

      RightClick() {
        $(document).on('contextmenu', function (e) {
          //在这里限制右键点击框出现的范围
          $('.context-menu').css({
            top: e.pageY,
            left: e.pageX,
            display: 'block'
          });

          $('h1').fadeOut('fast');

          return false;
        });

        $(document).click(function (e) {
          if (e.which == 1) {
            $('.context-menu').hide();
          }
        });

        $(document).keydown(function (e) {
          if (e.which == 27) {
            $('.context-menu').hide();
          }
        });
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .el-container {
    height: 100%;
  }

  .el-header {
    width: 100%;
    border-radius: 8px;
    background-color: #b2b9e2;
    padding-top: 18px;
  }

  .el-main {
    margin-top: 5px;
    margin-right: 5px;
    padding-top: 0;
    padding-left: 0;
    padding-right: 0;
  }

  .box {
    /*width: 1250px;*/
    border-radius: 10px;
    height: 670px;
    background: white;
    border: 3px solid #b8b3e2;
  }

  .container {
    width: 100%;
    border-radius: 8px;
    background: white;
  }

  #zoomOut {
    margin-bottom: 10px;
  }

  #zoomIn {
    margin-bottom: 10px;
  }

  #translateX_LEFT {
    margin-bottom: 10px;
  }

  #translateX_RIGHT {
    margin-bottom: 10px;
  }

  #translateY_UP {
    margin-bottom: 10px;
  }

  #translateY_DOWN {
    margin-bottom: 10px;
  }

  #reset {
    margin-bottom: 10px;
  }

  #refresh {
    margin-bottom: 10px;
    margin-right: 9px;
  }

  .operatePlatform {
    margin-top: 5px;
    height: 670px;
    border: 3px solid #b8b3e2;
    border-radius: 10px;
  }

  * {
    user-select: none;
  }

  body {
    cursor: default;
  }

  h1 {
    position: absolute;
    top: 50%;
    left: 50%;
    font-size: 50px;
    transform: translate(-50%, -50%);
  }

  .context-menu {
    position: absolute;
    top: 0;
    left: 0;
    background-color: white;
    padding: 10px;
    margin: 0;
    display: none;
    border: 1px solid black;
    border-radius: 3px;
    font-family: sans-serif;
    transition: all 0.3s;
  }

  .opt {
    padding: 0;
    margin: 0;
    list-style: none;
  }

  .opt_add {
    margin: 5px 10px;
    padding: 5px 10px;
  }

  .opt_add:hover {
    background-color: gray;
    color: white;
  }

  .opt_add:hover a, .opt_add:hover small {
    color: white;
  }

  .opt_1 {
    color: black;
    text-decoration: none;
    cursor: pointer;
  }


</style>
