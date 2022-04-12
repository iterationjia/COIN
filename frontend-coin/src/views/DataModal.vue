<template>
  <div style="padding-right: 10px;padding-left: 10px;padding-top: 10px;padding-bottom: 10px;height:610px">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="数据表" name="first">
        <!-- 表格 -->
        <el-table
          ref="filterTable"
          :data="(tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)).filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
          border
          @filter-change="filterTagTable"
          style="width: 100% ">
          <el-table-column
            prop="id"
            sortable
            label="ID"
            width="180">
          </el-table-column>
          <el-table-column
            prop="name"
            sortable
            label="名称">
          </el-table-column>
          <el-table-column
            filter-placement="bottom-end"
            :column-key=" 'shape' "
            :filters="shapeTableData"
            prop="shape"
            label="形状"
            width="180">
            <template slot-scope="scope">
            <span
              disable-transitions>{{toShape(scope.row.shape)}}</span>
            </template>
          </el-table-column>
          <el-table-column
            filter-placement="bottom-end"
            :column-key=" 'color' "
            prop="color"
            label="颜色">
            <template slot-scope="scope">
          <span
            disable-transitions>
            <el-tag
              :color="scope.row.color"
              size="medium"> </el-tag>
          </span>
            </template>
          </el-table-column>
          <el-table-column prop="node_size" label="大小（外接圆半径）">
          </el-table-column>
          <el-table-column
            align="right">
            <template slot="header" slot-scope="scope">
              <el-input
                v-model="search"
                size="mini"
                placeholder="输入关键字搜索"/>
            </template>
            <template slot-scope="scope">
              <el-button
                @click="checkSubNodes(scope.$index, scope.row)">查看子节点
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="block">
          <el-pagination align='center'
                         @size-change="handleSizeChange"
                         @current-change="handleCurrentChange"
                         :current-page="currentPage"
                         :page-size="pageSize"
                         layout="total,  prev, pager, next, jumper"
                         :total="tableData.length">
          </el-pagination>
        </div>
        <el-drawer
          title="我是标题"
          :visible.sync="drawer"
          :with-header="false"
          size="20%">
          <span><p style=" font-weight: bold">{{this.motherNode}}的子节点列表</p></span>
          <el-table
            :data="this.subNodeList"
            height="670"
            border
            style="width: 320px;">
            <el-table-column
              prop="name"
              label="名称"
              width="305">
            </el-table-column>
          </el-table>
        </el-drawer>
      </el-tab-pane>
      <el-tab-pane label="数据图谱" name="second">
        节点形状饼图
        <div class="container1">
        </div>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
  import {mapMutations, mapActions, mapGetters} from 'vuex'
  import html2canvas from "html2canvas";
  import * as d3 from 'd3';

  export default {
    name: "DataModal",
    props: {
      tableData_temp: {
        type: Array,
        required: true
      }
    },
    mounted() {
    },
    data() {
      return {
        activeName: 'first',
        currentPage: 1,
        pageSize: 7,
        selectdata: {shape: [], address: []},
        newData: [],
        twoData: [],
        data: [],
        tableData: this.tableData_temp,
        listdata: '',
        drawer: false,
        drawer_graph: false,
        search: '',
        motherNode: '',
        shapeTableData: [
          {text: '圆形', value: 'circle'},
          {text: '方形', value: 'rect'},
          {text: '三角形', value: 'triangle_up'},
          {text: '菱形', value: 'diamond'}
        ],
        listQuery: {
          status: ''
        },
        graph_shape_data: [
          {'shape': '方形', 'num': 20},
          {'shape': '三角形', 'num': 40},
          {'shape': '圆形', 'num': 90},
          {'shape': '菱形', 'num': 80}
        ],
      }
    },
    computed: {
      ...mapGetters([
        'currentNodeId',
        'subNodeList',
      ])
    },

    methods: {
      ...mapMutations([
        'set_subNodeList',
        'set_currentNodeId',
      ]),

      ...mapActions([
        'getSubNodes',
      ]),
      //过滤&&分页
      handleSizeChange(val) {
        this.pageSize = val
      },
      handleCurrentChange(val) {
        this.currentPage = val
      },
      filterTag(value, row) {
        return row.name === value
      },
      filterHandler(value, row, column) {
        const property = column['property'];
        console.log(this.tableData)
        return row[property] === value
      },
      filterTagTable(filters) {
        let filter = filters.shape;
        if (filter.length === 0) {
          this.tableData = this.tableData_temp;
          return;
        }
        this.tableData = [];
        for (let i = 0; i < this.tableData_temp.length; i++) {
          let temp = 0;
          for (let j = 0; j < filter.length; j++) {
            if (filter[j] === this.tableData_temp[i].shape) {
              temp = 1;
              break;
            }
          }
          if (temp === 1) {
            this.tableData.push(this.tableData_temp[i]);
          }
        }
      },

      async checkSubNodes(index, row) {
        this.drawer = true;
        this.motherNode = row.name;
        this.set_currentNodeId(row.id);
        await this.getSubNodes();
        console.log(this.subNodeList)
      },

      parentDrawPie() {
        this.drawPie(this.graph_shape_data);
      },

      parentRemove() {
        d3.selectAll(".container1 > *").remove();
      },

      drawPie(data) {
        // 将源数据转换为可以生成饼图的数据(有起始角度（startAngle）和终止角度（endAngle）)
        let piedata = d3.pie().value(function (d) {
          return d.num
        })(data);
        // 画布的参数
        let mapWidth = 300;
        let mapHeight = 300;
        let mapPadding = 20
        // 定义画布—— 宽 300 高 300 外边距 10px
        let svgMap = d3.select(".container1").append('svg').attr("width", mapWidth).attr("height", mapHeight).style("margin", "10px")
        //定义弧形生成器
        let innerRadius = 0;//内半径，为0则中间没有空白
        let outerRadius = mapWidth / 2 - mapPadding; //外半径
        let arc_generator = d3.arc()
          .innerRadius(innerRadius)
          .outerRadius(outerRadius);
        // 创建分组
        let groups = svgMap.selectAll("g")
          .data(piedata)
          .enter()
          // 添加分组
          .append("g")
          .attr("transform", "translate(" + (mapWidth / 2) + "," + (mapHeight / 2) + ")")
        // 绘制饼图——添加弧形路径
        groups.append("path")
          // 给路径填充不同的颜色
          .attr("fill", function (d, i) {
            //定义颜色比例尺，让不同的扇形呈现不同的颜色
            let colorScale = d3.scaleOrdinal()
              .domain(d3.range(piedata.length))
              .range(d3.schemeCategory10);
            return colorScale(i);
          })
          // 根据饼图数据，绘制弧形路径
          .attr("d", function (d) {
            return arc_generator(d);   //调用弧生成器，得到路径值
          })
        // 添加文字
        groups.append("text")
          .attr("transform", function (d) {//位置设在中心处
            return "translate(" + arc_generator.centroid(d) + ")";
          })
          .attr("text-anchor", "middle")
          .style('font-size', 10)
          .text(function (d) {
            return d.data.shape + ":" + d.data.num;
          })
      },
      handleClick(tab, event) {
      },

      toShape(shape) {
        switch (shape) {
          case 'circle':
            return '圆形'
          case 'rect':
            return '方形'
          case 'triangle_up':
            return '三角形'
          case 'diamond':
            return '菱形'
        }
      },

      //绘制饼状图之前统计各个形状节点数量
      dataset(data) {
        let circle_num = 0;
        let diamond_num = 0;
        let rect_num = 0;
        let triangle_num = 0;
        this.tableData.filter((item, i) => {
          if (item.shape === "triangle_up") {
            triangle_num++;
          }
          if (item.shape === "rect") {
            rect_num++;
          }
          if (item.shape === "diamond") {
            diamond_num++;
          }
          if (item.shape === "circle") {
            circle_num++;
          }
        });
        this.graph_shape_data[0].num = rect_num;
        this.graph_shape_data[1].num = triangle_num;
        this.graph_shape_data[2].num = circle_num;
        this.graph_shape_data[3].num = diamond_num;
      },

      set(data1) {
        this.tableData = data1;
      }
    }
  }
</script>

<style scoped>

</style>
