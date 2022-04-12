<template>
  <div>
    <div style="position: absolute;margin-left: 1255px;margin-top: 430px;box-shadow: 0 2px 12px 0 rgba(0,0,0,0.22)">
      <div style="top:5px">
        <el-button id="zoomOut_typeSetting" icon="el-icon-zoom-in"></el-button>
        <el-button id="translateY_UP_typeSetting" icon="el-icon-top"></el-button>
        <el-button id="zoomIn_typeSetting" icon="el-icon-zoom-out"></el-button>
      </div>
      <div>
        <el-button id="translateX_LEFT_typeSetting" icon="el-icon-back"></el-button>
        <el-button id="translateY_DOWN_typeSetting" icon="el-icon-bottom"></el-button>
        <el-button id="translateX_RIGHT_typeSetting" icon="el-icon-right"></el-button>
      </div>
      <div>
        <el-button id="reset_typeSetting" icon="el-icon-refresh-left"></el-button>
        <el-button class="save" @click="StoreTableVisible = true">保存图谱</el-button>
      </div>
    </div>
    <el-dialog
      :visible.sync="StoreTableVisible"
      width="30%">
               <span>
                 <el-button @click="StoreTableToPage">保存图片</el-button>
               </span>
      <span>
                 <el-button @click="StoreTableToData">导出数据</el-button>
               </span>
    </el-dialog>
    <div class="TypeSetContainer" id="TypeSetPic"></div>
  </div>
</template>
<script>
  import {mapMutations, mapActions, mapGetters} from 'vuex'
  import html2canvas from "html2canvas";
  import * as d3 from 'd3';
  import FileSaver from "file-saver";

  export default {
    name: "TypesettingModal",
    data() {
      return {
        svgArea: null,
        width: 1250,
        height: 530,
        StoreTableVisible: false,
        time: 1,
        links: [],
        nodes: [],
        rectNodes: [], //方形节点
        triangleUpNodes: [], //三角形节点
        diamondNodes: [], //菱形节点
        nodesName: [],
        linksName: [],
        simulation: null
      }
    },
    computed: {
      ...mapGetters([
        'customGraphRes',
        'currentGraphId',
      ])
    },

    methods: {
      ...mapMutations([]),

      ...mapActions([
        'getTypeSetting'
      ]),

      async getTypeSettingData() {
        let _this = this
        console.log(this.currentGraphId)
        await this.getTypeSetting()
        console.log(this.customGraphRes)
        let tempnodes = _this.customGraphRes.nodes;
        let templinks = _this.customGraphRes.links;
        for (let i = 0; i < templinks.length; i++) {
          for (let j = 0; j < tempnodes.length; j++) {
            if (templinks[i].source.id === tempnodes[j].id) {
              templinks[i].source = tempnodes[j]
            }
            if (templinks[i].target.id === tempnodes[j].id) {
              templinks[i].target = tempnodes[j]
            }
          }
        }
        _this.customGraphRes.links = templinks;
        _this.customGraphRes.nodes = tempnodes;

        this.typeInitGraph(_this.customGraphRes)
      },

      typeInitGraph(data) {

        const links1 = data.links
        const nodes1 = data.nodes
        var _this = this;
        _this.time = 1;

        this.simulation = d3.forceSimulation(nodes1)
          .force("link", d3.forceLink(links1).id(d => d.id).distance(200).strength(0))
          .force("collide", d3.forceCollide().radius(() => 50))
          .force("charge", d3.forceManyBody().strength(-40))
          .force("center", d3.forceCenter(this.width / 2, this.height / 2));

        let zoom = d3.zoom()
          .scaleExtent([0.00001, 20])
          .on("zoom", function (event) {
            g.attr("transform", event.transform)
          })

        this.svgArea = d3.select(".TypeSetContainer")
          .append("svg")
          .attr("viewBox", [0, 0, this.width, this.height])
          .attr("class", "svgCanvas")

        this.addMarkers();

        d3.select("#reset_typeSetting")
          .on("click", function () {
            g.call(zoom.transform, d3.zoomIdentity);
            _this.zoomOperate = [];
          })


        d3.select("#zoomIn_typeSetting")
          .on("click", function () {
            if (_this.time > 0.512) {
              zoom.scaleBy(g, 0.8);
              _this.time = _this.time * 0.8;
            }
          })

        d3.select("#zoomOut_typeSetting")
          .on("click", function () {
            if (_this.time < 1.728) {
              zoom.scaleBy(g, 1.2);
              _this.time = _this.time * 1.2;
            }
          })

        d3.select("#translateX_LEFT_typeSetting")
          .on("click", function (d) {
            zoom.translateBy(g, -20, 0);
          });

        d3.select("#translateX_RIGHT_typeSetting")
          .on("click", function (d) {
            zoom.translateBy(g, 20, 0);
          });

        d3.select("#translateY_UP_typeSetting")
          .on("click", function (d) {
            zoom.translateBy(g, 0, -20);
          })

        d3.select("#translateY_DOWN_typeSetting")
          .on("click", function (d) {
            zoom.translateBy(g, 0, 20);
          })

        const g = this.svgArea.append("g")
          .attr("class", "content")
        // .call(zoom)

        this.links = g.append("g")
          .selectAll("path")
          .data(links1, function (d) {
            if (typeof (d.source) === 'object') {
              return d.source.id + "_" + d.name + "_" + d.target.id
            } else {
              return d.source + "_" + d.name + "_" + d.target
            }
          })
          .join("path")
          .attr("stroke", "#999")
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
            d3.select(this).attr("stroke", "#838383").attr("stroke-width", 4);
          }).on('mouseout', function (d) {
            d3.select(this).attr("stroke", "#999").attr("stroke-width", 2);
          });

        this.linksName = g.append("g")
          .selectAll("text")
          .data(links1, function (d) {
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
          .text(d => d.name)
          .style("visibility", function (d) {
            if (d.shown) {
              return "visible"
            } else {
              return "hidden"
            }
          });

        this.DrawLine = this.svgArea.append("path")
          .attr("stroke", "#999")
          .attr("stroke-opacity", 0.8)
          .attr("marker-end", "url(resolved)")
          .attr("fill-opacity", 0)
          .attr("stroke-width", 3)
          .attr("marker-end", function (d) {
            return "url(#positiveMarker)"
          });

        let that = this;

        this.nodes = g.append("g")
          .attr("stroke", "#999")
          .attr("stroke-width", 2)
          .selectAll("circle")
          .data(nodes1)
          .join("circle")
          .filter(d => d.shape === "circle")
          .attr("r", d => d.node_size)
          .attr("class", "node")
          .attr("fill", d => d.color)
          .on("click", this.nodeClick)
          .on('mousedown', (event, d) => {
            event.preventDefault();
            this.mousedownNode = d;
            this.DrawLine.attr("stroke-width", 3).attr("marker-end", "url(resolved)").attr("marker-end", function (d) {
              return "url(#positiveMarker)";
            })
            this.moveStop = '';
          })
          .on('mouseup', (event, d) => {
            this.mouseupNode = d;
          })
          .on('mouseover', function (d) {
            this.mouseupNode = d;
            //当鼠标放在节点上时，高亮节点
            d3.select(this).attr("stroke", "#5d5d5d").attr("stroke-width", 3);
          }).on('mouseout', function (d) {
            //当鼠标不再放在节点上时，恢复节点
            d3.select(this).attr("stroke", "#999").attr("stroke-width", 2);
          });

        //方形、三角形、菱形节点初始化
        this.rectNodes = g.append("g")
          .attr("stroke", "#999")
          .attr("stroke-width", 2)
          .selectAll("rect")
          .data(nodes1)
          .enter()
          .filter(d => d.shape === "rect")
          .append("rect")
          .attr("width", d => d.node_size * 1.41) //方形为对应size原型的内切方形
          .attr("height", d => d.node_size * 1.41)
          .attr("class", "node")
          .attr("fill", d => d.color)
          .on("click", this.nodeClick)
          .on('mousedown', (event, d) => {
            event.preventDefault();
            this.DrawLine.attr("stroke-width", 3).attr("marker-end", "url(resolved)").attr("marker-end", function (d) {
              return "url(#positiveMarker)";
            })
            this.mousedownNode = d;
            this.moveStop = '';
          })
          .on('mouseup', (event, d) => {
            this.mouseupNode = d;
          })
          .on('mouseover', function (d) {
            this.mouseupNode = d;
            //当鼠标放在节点上时，高亮节点
            d3.select(this).attr("stroke", "#5d5d5d").attr("stroke-width", 3);
          }).on('mouseout', function (d) {
            //当鼠标不再放在节点上时，恢复节点
            d3.select(this).attr("stroke", "#999").attr("stroke-width", 2);
          });

        this.triangleUpNodes = g.append("g")
          .attr("stroke", "#999")
          .attr("stroke-width", 2)
          .selectAll("triangle_up")
          .data(nodes1)
          .enter()
          .filter(d => d.shape === "triangle_up")
          .append('path')
          .attr("class", "node")
          .attr("fill", d => d.color)
          .on("click", this.nodeClick)
          .on('mousedown', (event, d) => {
            event.preventDefault();
            this.DrawLine.attr("stroke-width", 3).attr("marker-end", "url(resolved)").attr("marker-end", function (d) {
              return "url(#positiveMarker)";
            })
            this.mousedownNode = d;
            this.moveStop = '';
          })
          .on('mouseup', (event, d) => {
            this.mouseupNode = d;
          })
          .on('mouseover', function (d) {
            this.mouseupNode = d;
            //当鼠标放在节点上时，高亮节点
            d3.select(this).attr("stroke", "#5d5d5d").attr("stroke-width", 3);
          }).on('mouseout', function (d) {
            //当鼠标不再放在节点上时，恢复节点
            d3.select(this).attr("stroke", "#999").attr("stroke-width", 2);
          });

        this.diamondNodes = g.append("g")
          .attr("stroke", "#999")
          .attr("stroke-width", 2)
          .selectAll("diamond")
          .data(nodes1)
          .enter()
          .filter(d => d.shape === "diamond")
          .append('path')
          .attr("class", "node")
          .attr("fill", d => d.color)
          .on("click", this.nodeClick)
          .on('mousedown', (event, d) => {
            event.preventDefault();
            this.DrawLine.attr("stroke-width", 3).attr("marker-end", "url(resolved)").attr("marker-end", function (d) {
              return "url(#positiveMarker)";
            })
            this.mousedownNode = d;
            this.moveStop = '';
          })
          .on('mouseup', (event, d) => {
            this.mouseupNode = d;
          })
          .on('mouseover', function (d) {
            this.mouseupNode = d;
            //当鼠标放在节点上时，高亮节点
            d3.select(this).attr("stroke", "#5d5d5d").attr("stroke-width", 3);
          }).on('mouseout', function (d) {
            //当鼠标不再放在节点上时，恢复节点
            d3.select(this).attr("stroke", "#999").attr("stroke-width", 2);
          });

        this.nodesName = g.append("g")
          .selectAll("text")
          .data(nodes1)
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
          .attr("fill", "black")
          .style("visibility", function (d) {
            if (d.shown) {
              return "visible"
            } else {
              return "hidden"
            }
          });

        //getCoord(d.target, d.source)  getCoord(d.source, d.target)

        //(d.target.node_size)/(Math.sqrt(Math.pow(d.target.typesetting_y-d.source.typesetting_y,2)+Math.pow(d.target.typesetting_x-d.source.typesetting_x,2)))

        this.simulation.on("tick", () => {
          this.links
            .attr("d", function (d) {
              if (d.source.typesetting_x < d.target.typesetting_x && d.source.typesetting_y < d.target.typesetting_y) {
                return ("M " +
                  (d.source.typesetting_x - (d.source.typesetting_x - d.target.typesetting_x) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.typesetting_y - d.target.typesetting_y, 2) + Math.pow(d.source.typesetting_x - d.target.typesetting_x, 2))))
                  + " " +
                  (d.source.typesetting_y - (d.source.typesetting_y - d.target.typesetting_y) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.typesetting_y - d.target.typesetting_y, 2) + Math.pow(d.source.typesetting_x - d.target.typesetting_x, 2))))
                  + " L " +
                  (d.target.typesetting_x - (d.target.typesetting_x - d.source.typesetting_x) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.typesetting_y - d.source.typesetting_y, 2) + Math.pow(d.target.typesetting_x - d.source.typesetting_x, 2))))
                  + " " +
                  (d.target.typesetting_y - (d.target.typesetting_y - d.source.typesetting_y) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.typesetting_y - d.source.typesetting_y, 2) + Math.pow(d.target.typesetting_x - d.source.typesetting_x, 2))))
                )
              } else if (d.source.typesetting_x < d.target.typesetting_x && d.source.typesetting_y >= d.target.typesetting_y) {
                return ("M " +
                  (d.source.typesetting_x - (d.source.typesetting_x - d.target.typesetting_x) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.typesetting_y - d.target.typesetting_y, 2) + Math.pow(d.source.typesetting_x - d.target.typesetting_x, 2))))
                  + " " +
                  (d.source.typesetting_y - (d.source.typesetting_y - d.target.typesetting_y) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.typesetting_y - d.target.typesetting_y, 2) + Math.pow(d.source.typesetting_x - d.target.typesetting_x, 2))))
                  + " L " +
                  (d.target.typesetting_x - (d.target.typesetting_x - d.source.typesetting_x) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.typesetting_y - d.source.typesetting_y, 2) + Math.pow(d.target.typesetting_x - d.source.typesetting_x, 2))))
                  + " " +
                  (d.target.typesetting_y - (d.target.typesetting_y - d.source.typesetting_y) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.typesetting_y - d.source.typesetting_y, 2) + Math.pow(d.target.typesetting_x - d.source.typesetting_x, 2))))
                )
              } else if (d.source.typesetting_x > d.target.typesetting_x && d.source.typesetting_y < d.target.typesetting_y) {
                return ("M " +
                  (d.target.typesetting_x - (d.target.typesetting_x - d.source.typesetting_x) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.typesetting_y - d.source.typesetting_y, 2) + Math.pow(d.target.typesetting_x - d.source.typesetting_x, 2))))
                  + " " +
                  (d.target.typesetting_y - (d.target.typesetting_y - d.source.typesetting_y) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.typesetting_y - d.source.typesetting_y, 2) + Math.pow(d.target.typesetting_x - d.source.typesetting_x, 2))))
                  + " L " +
                  (d.source.typesetting_x - (d.source.typesetting_x - d.target.typesetting_x) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.typesetting_y - d.target.typesetting_y, 2) + Math.pow(d.source.typesetting_x - d.target.typesetting_x, 2))))
                  + " " +
                  (d.source.typesetting_y - (d.source.typesetting_y - d.target.typesetting_y) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.typesetting_y - d.target.typesetting_y, 2) + Math.pow(d.source.typesetting_x - d.target.typesetting_x, 2))))
                )
              } else {
                return ("M " +
                  (d.target.typesetting_x - (d.target.typesetting_x - d.source.typesetting_x) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.typesetting_y - d.source.typesetting_y, 2) + Math.pow(d.target.typesetting_x - d.source.typesetting_x, 2))))
                  + " " +
                  (d.target.typesetting_y - (d.target.typesetting_y - d.source.typesetting_y) * (d.target.node_size) / (Math.sqrt(Math.pow(d.target.typesetting_y - d.source.typesetting_y, 2) + Math.pow(d.target.typesetting_x - d.source.typesetting_x, 2))))
                  + " L " +
                  (d.source.typesetting_x - (d.source.typesetting_x - d.target.typesetting_x) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.typesetting_y - d.target.typesetting_y, 2) + Math.pow(d.source.typesetting_x - d.target.typesetting_x, 2))))
                  + " " +
                  (d.source.typesetting_y - (d.source.typesetting_y - d.target.typesetting_y) * (d.source.node_size) / (Math.sqrt(Math.pow(d.source.typesetting_y - d.target.typesetting_y, 2) + Math.pow(d.source.typesetting_x - d.target.typesetting_x, 2))))
                )
              }

              //else if(d.source.typesetting_x === d.target.typesetting_x){
              //   return "M" + (d.target.typesetting_x + 20) + " " + (d.target.typesetting_y - 10) + " C " + (d.target.typesetting_x + 120) + " " + (d.target.typesetting_y - 60)
              //     + " " + (d.target.typesetting_x + 120) + " " + (d.target.typesetting_y + 60) + "," + (d.target.typesetting_x + 20) + " " + (d.target.typesetting_y+ 10);
              //}
            })
            .attr("marker-end", function (d) {
              if (d.source.typesetting_x < d.target.typesetting_x) {
                return "url(#positiveMarker)"
              } else {
                return null
              }
            })
            .attr("marker-start", function (d) {
              if (d.source.typesetting_x < d.target.typesetting_x) {
                return null
              } else {
                return "url(#negativeMarker)"
              }
            })

          this.nodes
            .attr("cx", d => d.typesetting_x)
            .attr("cy", d => d.typesetting_y);

          this.rectNodes
            .attr("x", d => d.typesetting_x - d.node_size * 0.707) //方形中心点
            .attr("y", d => d.typesetting_y - d.node_size * 0.707);

          this.diamondNodes
            .attr("d", function (d) {
              return "M " + (d.typesetting_x - d.node_size) + " " + (d.typesetting_y) + " L " + (d.typesetting_x + d.node_size - d.node_size) + " " + (d.typesetting_y + d.node_size) + " L " + (d.typesetting_x + 2 * d.node_size - d.node_size) + " " + (d.typesetting_y) + " L " + (d.typesetting_x + d.node_size - d.node_size) + " " + (d.typesetting_y - d.node_size) + " L " + (d.typesetting_x - d.node_size) + " " + (d.typesetting_y)
            });

          this.triangleUpNodes
            .attr("d", function (d) {
              return "M " + (d.typesetting_x - 0.87 * d.node_size) + " " + (d.typesetting_y + 0.5 * d.node_size) + " L " + (d.typesetting_x + 0.87 * d.node_size - 0.87 * d.node_size) + " " + (d.typesetting_y - 1.5 * d.node_size + 0.5 * d.node_size) + " L " + (d.typesetting_x + d.node_size * 1.73 - 0.87 * d.node_size) + " " + (d.typesetting_y + 0.5 * d.node_size) + " L " + (d.typesetting_x - 0.87 * d.node_size) + " " + (d.typesetting_y + 0.5 * d.node_size)
            });

          this.nodesName
            .attr("x", d => d.typesetting_x)
            .attr("y", d => d.typesetting_y)
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
          .attr("refX", 11)
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
          .attr("refX", 0)
          .attr("refY", 0)
          .attr("markerWidth", 12)
          .attr("markerHeight", 12)
          .append("path")
          .attr("d", "M 10 -5 L 0 0 L 10 5")
          .attr('fill', '#999')
          .attr("stroke-opacity", 0.6);
      },

      removeSvg() {
        d3.selectAll(".TypeSetContainer > *").remove();
      },

      //保存图谱
      StoreTableToPage() {
        html2canvas(document.getElementById('TypeSetPic'), {
          backgroundColor: 'white',
          useCORS: true, //支持图片跨域
          scale: 1, //设置放大的倍数
        })
          .then((canvas) => {
            // 生成图片导出
            const a = document.createElement('a');
            a.href = canvas.toDataURL('image/png');
            a.download = "排版模式";
            a.click();
          });

        this.StoreTableVisible = false;
      },

      StoreTableToData() {
        const data = JSON.stringify(this.customGraphRes.nodes);
        const blob = new Blob([data], {type: ''});
        FileSaver.saveAs(blob, "排版模式" + "_nodes")

        const data1 = JSON.stringify(this.customGraphRes.links);
        const blob1 = new Blob([data1], {type: ''});
        FileSaver.saveAs(blob1, "排版模式" + "_links")
      },
    }
  }
</script>

<style scoped>
  #zoomOut_typeSetting {
    margin-bottom: 10px;
    margin-top: 10px;
    margin-left: 10px;
  }

  #zoomIn_typeSetting {
    margin-bottom: 10px;
    margin-top: 10px;
    margin-right: 10px;
  }

  #translateX_LEFT_typeSetting {
    margin-bottom: 10px;
  }

  #translateX_RIGHT_typeSetting {
    margin-bottom: 10px;
  }

  #translateY_UP_typeSetting {
    margin-bottom: 10px;
    margin-top: 10px;
  }

  #translateY_DOWN_typeSetting {
    margin-bottom: 10px;
  }

  #reset_typeSetting {
    margin-bottom: 10px;
  }


</style>
