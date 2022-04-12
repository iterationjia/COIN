<template>
  <div>
    <!-- 创建关系-->
    <el-dialog
      title="创建新关系"
      :visible.sync="AddRelationVisible"
      width="30%">
            <span>
              <el-input v-model="input_Relation" placeholder="关系名"></el-input>
            </span>
      <el-select v-model="isSolid" placeholder="实线" style="float:left;width: 47% ;margin-top: 10px;margin-bottom: 10px">
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
    <SearchBoard v-show="searchBoardVisible" v-bind:nodes="nodes" v-bind:rectNodes="rectNodes"
                 v-bind:triangleUpNodes="triangleUpNodes" v-bind:diamondNodes="diamondNodes" v-bind:links="links"
                 v-bind:LocalData="customGraphRes" v-bind:linksName="linksName"
                 v-bind:nodesName="nodesName"></SearchBoard>
    <NodeClick :getAllData="getCustomGraph" :removeSvg="removeSvg" :initGraph="initGraph"
               :numericalUpdate="numericalUpdate"
               v-show="nodeClickVisible"></NodeClick>
    <RelationClick :getAllData="getCustomGraph" :removeSvg="removeSvg" :initGraph="initGraph"
                   :numericalUpdate="numericalUpdate"
                   v-show="relationClickVisible"></RelationClick>
    <Numerical v-show="numericalVisible" v-if="update"></Numerical>
    <div class="forceMapContainer"></div>
  </div>
</template>

<script>
  import {mapMutations, mapActions, mapGetters} from 'vuex'
  import * as d3 from 'd3'
  import NodeClick from "../Components/NodeClick";
  import RelationClick from "../Components/RelationClick";
  import SearchBoard from "../Components/SearchBoard";
  import Numerical from "../Components/Numerical";

  export default {
    name: "ForceMap",
    components: {
      Numerical,
      SearchBoard,
      RelationClick,
      NodeClick
    },
    data() {
      return {
        update: true,

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
        simulation: null,
        width: 1250,
        height: 520,
        links: [],
        nodes: [],
        rectNodes: [], //方形节点
        triangleUpNodes: [], //三角形节点
        diamondNodes: [], //菱形节点
        nodesName: [],
        linksName: [],
        zoom: '',
      }
    },
    computed: {
      ...mapGetters([
        'customGraphRes',
        'customNodeParams',
        'customRelationParams',
        'customGraphParams',
        'nodeClickVisible',
        'relationClickVisible',
        'currentGraphId',
        'searchBoardVisible',
        'numericalVisible',
        'userGraphList',
      ])
    },
    async mounted() {
      await this.set_currentGraphId(parseInt(localStorage.getItem('graphId')))
      await this.getCustomGraph()
      await this.initGraph(this.customGraphRes)
      await this.set_userParams({
        id: localStorage.getItem('userId')
      })
      await this.getUserGraphList()
      for (let i = 0; i < this.userGraphList.length; i++) {
        if (this.userGraphList[i].id === parseInt(this.currentGraphId)) {
          await this.set_customGraphParams(this.userGraphList[i])
          break;
        }
      }
    },
    methods: {
      ...mapMutations([
        'set_nodeClickVisible',
        'set_relationClickVisible',
        'set_customNodeParams',
        'set_customRelationParams',
        'set_currentGraphId',
        'set_customRelationParams',
        'set_customGraphParams',
        'set_userParams',
      ]),

      ...mapActions([
        'getCustomGraph',
        'getAllGraph',
        'addCustomRelation',
        'getUserGraphList',
      ]),

      initGraph(data) {
        console.log('init!')
        const links = data.links
        const nodes = data.nodes
        const _this = this;
        _this.time = 1;
        _this.zoomOperate = [];

        this.simulation = d3.forceSimulation(nodes)
          .force("link", d3.forceLink(links).id(d => d.id).distance(200).strength(0))
          .force("collide", d3.forceCollide().radius(() => 50))
          .force("charge", d3.forceManyBody().strength(-40))
          .force("center", d3.forceCenter(this.width / 2, this.height / 2));

        this.zoom = d3.zoom()
          .filter(() => this.flagAddRelation === 'false')
          .scaleExtent([0.25, 4])
          .translateExtent([[-4000, -4000], [this.width + 4000, this.height + 4000]])
          .on("zoom", function (event) {
            g.attr("transform", event.transform)
          })

        d3.select(".box")
          .attr("preserveAspectRatio", "xMinYMin meet")
          .call(this.zoom);


        this.svgArea = d3.select(".forceMapContainer")
          .append("svg")
          .attr("viewBox", [0, 0, this.width, this.height])
          .attr("class", "svgCanvas")

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
            if (d.shown && _this.customGraphParams.isRelationLabelVisible) {
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

        this.simulation.on("tick", () => {
          this.links
            .attr("d", function (d) {
              if ((d.source.shape != 'circle') && (d.target.shape != 'circle')) {
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
              if ((d.source.shape != 'circle') && (d.target.shape === 'circle')) {
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
              if ((d.source.shape === 'circle') && (d.target.shape != 'circle')) {
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
        d3.selectAll(".forceMapContainer > *").remove();
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

      // 添加关系
      async handleAddRelationClose() {
        const data = {
          id: 0,
          label: '',
          source: this.mousedownNode.id,
          target: this.mouseupNode.id,
          graphId: parseInt(this.currentGraphId),
          name: this.input_Relation,
          shown: this.isShow,
          solid: this.isSolid,
          isHighlighted: false,
        }
        await this.set_customRelationParams(data)
        await this.addCustomRelation()
        await this.getCustomGraph()
        await this.removeSvg()
        await this.initGraph(this.customGraphRes)

        this.zoomOperate = [];

        this.flagAddRelation = 'false'
        this.AddRelationVisible = false;
        // 清空
        this.mouseupNode = ''
        this.mousedownNode = ''
        this.input_Relation = ''
        this.numericalUpdate()
      },

      nodeClick(d, i) {
        if (this.flagAddRelation === 'false') {
          this.set_nodeClickVisible(true);
          this.set_customNodeParams(i);
        }
      },

      linkClick(d, i) {
        this.set_relationClickVisible(true);
        this.set_customRelationParams(i);
      },

      //返回按钮前缩小图谱
      SaveAndQuit_ZoomIn() {
        var zoomTest = d3.zoomIdentity;
        zoomTest.k = 0.8;
        //zoomTest.x=100;
        //zoomTest.y=100;
        d3.select(".box")
          .attr("preserveAspectRatio", "xMinYMin meet")
          .transition().duration(0).call(this.zoom.transform, zoomTest);
      },

      reset() {
        var zoomTest = d3.zoomIdentity;
        zoomTest.k = 1;
        console.log(zoomTest)
        d3.select(".box")
          .attr("preserveAspectRatio", "xMinYMin meet")
          .transition().duration(500).call(this.zoom.transform, zoomTest);
        zoomTest.k = 1;
      },

      numericalUpdate() {
        this.update = false;
        this.$nextTick(() => {
          this.update = true
        })
      }
    },
  }
</script>

<style scoped>

</style>
