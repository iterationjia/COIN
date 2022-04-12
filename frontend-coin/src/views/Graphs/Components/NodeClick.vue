<template>
  <div class="dialog_node_info">
    <p style="height: 20px ;font-weight: 700">{{customNodeParams.name}}</p>

    <el-button style="width: 100px;margin-top: 15px" @click="beforeChange">修改信息</el-button>
    <el-dialog
      title="输入新信息"
      :visible.sync="changeObjectVisible"
      width="30%">

      <span>
              <el-input v-model="input_ChangeObject" placeholder="对象名"></el-input>
            </span>

      <span>
                 <el-select v-model="ChangeShapeValue" placeholder="形状" style="width: 48%;float: left;margin-top: 10px">
                <el-option
                  v-for="item in ChangeShapOptions"
                  :key="item.ChangeShapeValue"
                  :label="item.ChangeShapeLabel"
                  :value="item.ChangeShapeValue">
                </el-option>
              </el-select>
              <el-input v-model="input_ChangeNodeSize" placeholder="节点大小(外接圆半径)"
                        style="width: 48%;float: left;margin-top: 10px;margin-left: 4%;"></el-input>
            </span>
      <span>
                 <div class="ColorBlock" style="width: 48%;float: left;margin-top: 10px">
                   <p style="float: left;width: 35%;margin-left: 10%;margin-bottom: 10px">节点颜色:</p>
                  <el-color-picker v-model="input_ChangeNodeColor"
                                   style="float: left;margin-left: 10%;margin-bottom: 10px"></el-color-picker>
                  </div>
              <el-input v-model="input_ChangeFontSize" placeholder="字体大小"
                        style="width: 48%;float: left;margin-left: 4%;margin-top: 10px;margin-bottom: 10px"></el-input>
            </span>

      <span slot="footer" class="dialog-footer">
            <el-button @click="changeObjectVisible=false">取 消</el-button>
            <el-button type="primary" @click="handleChangeObjectClose">确 定</el-button>
            </span>
    </el-dialog>

    <div>
      <el-popover
        placement="top"
        width="160"
        v-model="confirm_deleteNodeVisible">
        <p>确定删除这个对象吗</p>
        <div style="text-align: right; margin: 0">
          <el-button size="mini" type="text" @click="confirm_deleteNodeVisible = false">取消</el-button>
          <el-button type="primary" size="mini" @click="confirmToDeleteNode">确定</el-button>
        </div>
        <el-button slot="reference" type="danger" style="width: 100px;margin-top: 15px">删除对象</el-button>
      </el-popover>
    </div>
    <el-button icon="el-icon-close" circle
               style="margin-top: 15px;margin-bottom: 10px;background-repeat:no-repeat ;border: 0;background-color: transparent;border-style: none;"
               @click="set_nodeClickVisible(false)"></el-button>
  </div>
</template>

<script>
  import {mapMutations, mapActions, mapGetters} from 'vuex'

  export default {
    name: "NodeClick",
    data() {
      return {
        //增加子对象
        input_SubObject: '',
        SubShapOptions: [{
          SubShapeValue: 'circle',
          SubShapeLabel: '圆形'
        }, {
          SubShapeValue: 'rect',
          SubShapeLabel: '方形'
        }, {
          SubShapeValue: 'diamond',
          SubShapeLabel: '菱形'
        }, {
          SubShapeValue: 'triangle_up',
          SubShapeLabel: '三角形'
        }],
        SubShapeValue: '',
        input_SubNodeSize: '',
        input_SubFontSize: '',
        input_SubNodeColor: '',

        //修改信息
        input_ChangeObject: '',
        ChangeShapOptions: [{
          ChangeShapeValue: 'circle',
          ChangeShapeLabel: '圆形'
        }, {
          ChangeShapeValue: 'rect',
          ChangeShapeLabel: '方形'
        }, {
          ChangeShapeValue: 'diamond',
          ChangeShapeLabel: '菱形'
        }, {
          ChangeShapeValue: 'triangle_up',
          ChangeShapeLabel: '三角形'
        }],
        ChangeShapeValue: '',
        input_ChangeNodeSize: '',
        input_ChangeFontSize: '',
        input_ChangeNodeColor: '',

        addSubObjectVisible: false,
        changeObjectVisible: false,
        confirm_deleteNodeVisible: false,
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
      },
      numericalUpdate: {
        type: Function,
        default: null,
      }
    },

    computed: {
      ...mapGetters([
        'customNodeParams',
        'currentNodeId',
        'nodeClickVisible',
        'currentGraphId',
        'customGraphRes',
      ]),
    },

    methods: {
      ...mapMutations([
        'set_nodeClickVisible',
        'set_customNodeParams',
        'set_customRelationParams',
        'set_currentNodeId',
      ]),

      ...mapActions([
        'addCustomNode',
        'addCustomRelation',
        'deleteNode',
        'editCustomNode',
      ]),

      beforeChange() {
        this.input_ChangeObject = this.customNodeParams.name
        this.ChangeShapeValue = this.customNodeParams.shape
        this.input_ChangeNodeColor = this.customNodeParams.color
        this.input_ChangeFontSize = this.customNodeParams.font_size
        this.input_ChangeNodeSize = this.customNodeParams.node_size

        this.changeObjectVisible = true
      },

      async handleChangeObjectClose() {

        let nodeData = {
          id: this.customNodeParams.id,
          graph_id: 0,
          name: this.input_ChangeObject,
          typesetting_x: 0,
          typesetting_y: 0,
          shape: this.ChangeShapeValue,
          color: this.input_ChangeNodeColor,
          node_size: this.input_ChangeNodeSize,
          font_size: this.input_ChangeFontSize,
          isShown: true,
          isHighlighted: this.customNodeParams.highlighted,
        }

        await this.set_customNodeParams(nodeData)
        await this.editCustomNode()

        //清空
        this.input_ChangeObject = '';
        this.input_ChangeNodeColor = '';
        this.ChangeShapeValue = '';
        this.input_ChangeNodeSize = '';
        this.input_ChangeFontSize = '';

        await this.getAllData();

        this.removeSvg();
        this.initGraph(this.customGraphRes);
        this.changeObjectVisible = false;
        this.set_nodeClickVisible(false);
        this.numericalUpdate()
      },

      async confirmToDeleteNode() {
        this.confirm_deleteNodeVisible = false
        this.set_currentNodeId(this.customNodeParams.id)
        await this.deleteNode()
        await this.getAllData()
        this.removeSvg();
        this.initGraph(this.customGraphRes);
        this.set_nodeClickVisible(false)
        this.confirm_deleteNodeVisible = false
        this.numericalUpdate()
      },
    }
  }
</script>

<style scoped>
  .dialog_node_info {
    margin-top: 10px;
    margin-left: 10px;
    position: absolute;
    width: 190px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    border-radius: 4px;
    text-align: center;
  }
</style>
