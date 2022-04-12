<template>
  <div>
    <!--    <el-table-->
    <!--         :data="this.graphList"-->
    <!--         border-->
    <!--         @cell-click="clickTable"-->
    <!--         style="width: 100%;">-->
    <!--         <el-table-column-->
    <!--           label="图谱列表"-->
    <!--           prop="name"-->
    <!--           align="center"-->
    <!--         >-->
    <!--         </el-table-column>-->
    <!--       </el-table>-->
    <el-select style="width: 95%" v-model="currentGraphName" placeholder="请选择当前图谱" @change="clickTable">
      <el-option
        v-for="item in graphList"
        :key="item.id"
        :label="item.name"
        :value="item.id">
      </el-option>
    </el-select>

    <el-button @click="CreateChartVisible = true" style="margin-top: 7px">创建新的图谱</el-button>


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
    <el-button style="margin-top: 7px;float: left;margin-left: 5px" @click="StoreTableVisible = true">保存图谱</el-button>
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

    <el-upload
      :limit="1"
      :multiple="false"
      :on-success="afterUpload"
      :on-error="uploadError"
      accept=".txt"
      class="upload-demo"
      :action=this.uploadFileLocation>
      <el-button style="margin-top: 7px;float: left;">上传图谱</el-button>
    </el-upload>

    <el-button @click="deleteCurrentGraph">删除当前图谱</el-button>

    <el-button @click="beforeEdit" style="margin-top: 7px;margin-right: 9px">编辑当前图谱</el-button>
    <el-dialog :visible.sync="editGraphVisible" width="30%">
      <el-input v-model="input_chart" placeholder="请输入图谱名"></el-input>
      <span>
          <el-switch v-model="isNodeShowVal" active-text="节点标识显示" style="margin-top: 10px"></el-switch>
        </span>
      <span>
          <el-switch v-model="isRelationShowVal" active-text="连接标识显示" style="margin-top: 10px"></el-switch>
        </span>
      <span slot="footer" class="dialog-footer">
      <el-button @click="editGraphVisible = false">取 消</el-button>
      <el-button type="primary" @click="handleEditGraphClose">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import {mapMutations, mapActions, mapGetters} from 'vuex'
  import html2canvas from "html2canvas";
  import FileSaver from 'file-saver';

  export default {
    name: "Aside",
    data() {
      return {
        chartTableData: [{
          id: 1,
          name: '一号图',
          isRelationLabelVisible: true,
          isNodeLabelVisible: true,
        }, {
          id: 2,
          name: '二号图',
          isRelationLabelVisible: true,
          isNodeLabelVisible: true,
        }],
        input_chart: '',
        editGraphVisible: false,
        CreateChartVisible: false,
        StoreTableVisible: false,
        isNodeShowVal: true,
        isRelationShowVal: true,
        uploadFileLocation: '/api/api/customGraph/loadGraph',
        graphName: '',
      }
    },

    props: {
      getAllData: {
        type: Function,
        default: null,
      },
      removeSvg: {
        type: Function,
        default: null,
      },
      initGraph: {
        type: Function,
        default: null,
      }
    },

    computed: {
      ...mapGetters([
        'graphList',
        'customGraphRes',
        'customGraphParams',
        'currentGraphId',
        'currentGraphName',
      ])
    },

    methods: {

      ...mapMutations([
        'set_currentGraphId',
        'set_currentGraphName',
        'set_customGraphParams',
      ]),

      ...mapActions([
        'addGraph',
        'getAllGraph',
        'deleteGraph',
        'editGraph',
      ]),

      async clickTable(data) {
        let graph = this.graphList.find((n) => n.id === data)
        console.log(graph)
        this.set_currentGraphId(graph.id);
        this.set_currentGraphName(graph.name);
        await this.getAllData();
        await this.removeSvg();
        await this.initGraph(this.customGraphRes);
      },

      async handleCreateChartClose() {
        this.set_currentGraphName(this.input_chart);
        await this.addGraph();

        await this.getAllData();
        await this.removeSvg();
        await this.initGraph(this.customGraphRes);

        this.CreateChartVisible = false;
        this.input_chart = '';

        await this.getAllGraph();
      },

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
            a.download = this.currentGraphName;
            a.click();
          });

        this.StoreTableVisible = false;
      },

      StoreTableToData() {
        console.log()
        const data = JSON.stringify(this.customGraphRes.nodes);
        const blob = new Blob([data], {type: ''});
        FileSaver.saveAs(blob, this.currentGraphName + "_nodes")

        const data1 = JSON.stringify(this.customGraphRes.links);
        const blob1 = new Blob([data1], {type: ''});
        FileSaver.saveAs(blob1, this.currentGraphName + "_links")
      },

      async afterUpload() {
        await this.getAllGraph()
      },

      uploadError() {
        this.$message.error('upload failed')
      },

      // 删除当前图谱，如果删除后图谱列表为0，新建图谱
      async deleteCurrentGraph() {
        await this.deleteGraph();
        await this.getAllGraph();
        if (this.graphList.length === 0) {
          this.set_currentGraphName('new');
          await this.addGraph();
          await this.getAllGraph();
        }
        await this.getAllGraph()
        this.set_customGraphParams(this.graphList[0])
        this.set_currentGraphName(this.customGraphParams.name)
        this.set_currentGraphId(this.customGraphParams.id)
        await this.getAllData()
        this.removeSvg()
        this.initGraph(this.customGraphRes)
      },

      beforeEdit() {
        this.input_chart = this.currentGraphName
        this.isNodeShowVal = this.customGraphParams.isNodeLabelVisible
        this.isRelationShowVal = this.customGraphParams.isRelationLabelVisible
        this.editGraphVisible = true
      },

      async handleEditGraphClose() {
        let data = {
          id: this.customGraphParams.id,
          name: this.input_chart,
          isNodeLabelVisible: this.isNodeShowVal,
          isRelationLabelVisible: this.isRelationShowVal
        }

        this.set_customGraphParams(data)

        this.set_currentGraphName(data.name)
        await this.editGraph()
        await this.getAllGraph()
        this.removeSvg()
        this.initGraph(this.customGraphRes)

        this.input_chart = ''
        this.editGraphVisible = false
      }
    }
  }
</script>

<style scoped>
  .el-aside {
    margin-top: 5px;
    border: 3px solid #b8b3e2;
    border-radius: 10px;
    background-color: white;
  }
</style>
