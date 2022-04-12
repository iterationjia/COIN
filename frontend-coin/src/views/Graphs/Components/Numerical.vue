<template>
  <div class="dialog_node_info">
    <p style="height: 20px ;font-weight: 700">{{customGraphParams.name}}</p>
    <div id="nodePieReport" style="width: 240px;height: 180px;"></div>
    <div id="linkPieReport" style="width: 240px;height: 180px;"></div>
    <el-button icon="el-icon-close" circle
               style="margin-top: 15px;margin-bottom: 10px;background-repeat:no-repeat ;border: 0;background-color: transparent;border-style: none;"
               @click="set_numericalVisible(false)"></el-button>
  </div>
</template>

<script>
  import {mapGetters, mapActions, mapMutations} from 'vuex'

  export default {
    name: "Numerical",
    data() {
      return {
        nodeNumber: [
          {value: 0, name: "圆形", itemStyle: ""},
          {value: 0, name: "方形", itemStyle: ""},
          {value: 0, name: "三角形", itemStyle: ""},
          {value: 0, name: "菱形", itemStyle: ""},
        ],
        linkNumber: [
          {value: 0, name: "实线", itemStyle: ""},
          {value: 0, name: "虚线", itemStyle: ""},
        ]
      }
    },
    computed: {
      ...mapGetters([
        'customGraphParams',
        'customGraphRes',
        'numericalVisible',
      ])
    },
    mounted() {
      let nodes = this.customGraphRes.nodes
      let links = this.customGraphRes.links
      for (let i = 0; i < nodes.length; i++) {
        switch (nodes[i].shape) {
          case "rect":
            this.nodeNumber[1].value += 1;
            break;
          case "circle":
            this.nodeNumber[0].value += 1;
            break;
          case "triangle_up":
            this.nodeNumber[2].value += 1;
            break;
          case "diamond":
            this.nodeNumber[3].value += 1;
            break;
          default:
            break;
        }
      }
      for (let i = 0; i < links.length; i++) {
        if (links[i].solid) {
          this.linkNumber[0].value += 1;
        } else {
          this.linkNumber[1].value += 1;
        }
      }
      this.drawNode("nodePieReport")
      this.drawLink("linkPieReport")
    },
    methods: {
      ...mapMutations([
        'set_numericalVisible',
      ]),

      ...mapActions([]),

      drawNode(id) {
        this.charts = this.$echarts.init(document.getElementById(id));
        this.charts.setOption({
          tooltip: {
            trigger: "item",
            formatter: "{a}<br/>{b}:{c} ({d}%)"
          },
          series: [
            {
              name: "节点",
              type: "pie",
              radius: "65%",
              center: ["50%", "50%"],
              avoidLabelOverlap: false,
              itemStyle: {
                emphasis: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: "rgba(0, 0, 0, 0.5)"
                },
                color: function (params) {
                  //自定义颜色
                  var colorList = ["#1ab394", "#79d2c0", "#2AD394", "#AAd2c0"];
                  return colorList[params.dataIndex];
                }
              },
              data: this.nodeNumber
            }
          ]
        });
      },

      drawLink(id) {
        this.charts = this.$echarts.init(document.getElementById(id));
        this.charts.setOption({
          tooltip: {
            trigger: "item",
            formatter: "{a}<br/>{b}:{c} ({d}%)"
          },
          series: [
            {
              name: "连接线",
              type: "pie",
              radius: "65%",
              center: ["50%", "50%"],
              avoidLabelOverlap: false,
              itemStyle: {
                emphasis: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: "rgba(0, 0, 0, 0.5)"
                },
                color: function (params) {
                  //自定义颜色
                  var colorList = ["#1ab394", "#79d2c0"];
                  return colorList[params.dataIndex];
                }
              },
              data: this.linkNumber
            }
          ]
        });
      }
    }
  }
</script>

<style scoped>
  .dialog_node_info {
    margin-top: 10px;
    margin-left: 10px;
    position: absolute;
    width: 240px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    border-radius: 4px;
    text-align: center;
  }
</style>
