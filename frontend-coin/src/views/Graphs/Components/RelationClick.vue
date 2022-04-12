<template>
  <div class="dialog_link_info">
    <p style="height: 20px ;font-weight: 700">{{customRelationParams.source.name}}</p>
    <p style="height: 20px ">to</p>
    <p style="height: 20px ;font-weight: 700">{{customRelationParams.target.name}}</p>

    <el-button style="width: 100px;margin-top: 15px" @click="beforeChangeLink">修改关系</el-button>
    <el-dialog
      title="输入新关系"
      :visible.sync="changeLinkVisible"
      width="30%">
            <span>
              <el-input v-model="input_ChangeLink" placeholder="请输入新关系名"></el-input>
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
            <el-button @click="changeLinkVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleChangeLinkClose">确 定</el-button>
            </span>
    </el-dialog>
    <div>

      <el-popover
        placement="top"
        width="160"
        v-model="confirm_deleteLinkVisible">
        <p>确定删除这个关系吗</p>
        <div style="text-align: right; margin: 0">
          <el-button size="mini" type="text" @click="confirm_deleteLinkVisible = false">取消</el-button>
          <el-button type="primary" size="mini" @click="confirmToDeleteLink">确定</el-button>
        </div>
        <el-button slot="reference" type="danger" style="width: 100px;margin-top: 15px">删除关系</el-button>
      </el-popover>
    </div>
    <el-button icon="el-icon-close" circle
               style="margin-top: 15px;margin-bottom: 10px;background-repeat:no-repeat ;border: 0;background-color: transparent;border-style: none;"
               @click="set_relationClickVisible(false)"></el-button>
  </div>
</template>

<script>
  import {mapMutations, mapActions, mapGetters} from 'vuex'

  export default {
    name: "RelationClick",
    data() {
      return {
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

        changeLinkVisible: false,
        confirm_deleteLinkVisible: false,
        input_ChangeLink: '',
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
        'customRelationParams',
        'relationClickVisible',
        'customGraphRes',
      ]),
    },

    methods: {
      ...mapMutations([
        'set_relationClickVisible',
        'set_customRelationParams',
        'set_currentLink',
      ]),

      ...mapActions([
        'deleteRelation',
        'editRelation'
      ]),

      beforeChangeLink() {
        this.input_ChangeLink = this.customRelationParams.name
        this.isShow = this.customRelationParams.shown.toString()
        this.isSolid = this.customRelationParams.solid.toString()

        this.changeLinkVisible = true
      },

      async handleChangeLinkClose() {
        let data = {
          id: this.customRelationParams.id,
          source: this.customRelationParams.source.id,
          target: this.customRelationParams.target.id,
          graphId: this.customRelationParams.graphId,
          name: this.input_ChangeLink,
          shown: this.isShow,
          solid: this.isSolid,
          isHighlighted: this.customRelationParams.highlighted,
        }

        await this.set_customRelationParams(data)
        await this.editRelation()

        this.changeLinkVisible = false
        this.set_relationClickVisible(false)

        this.input_ChangeLink = ''

        await this.getAllData()
        await this.removeSvg()
        await this.initGraph(this.customGraphRes)
        this.numericalUpdate()
      },

      async confirmToDeleteLink() {
        await this.set_currentLink(this.customRelationParams.id)
        await this.deleteRelation()

        await this.getAllData()
        await this.removeSvg()
        await this.initGraph(this.customGraphRes)

        this.set_relationClickVisible(false);
        this.confirm_deleteLinkVisible = false;
        this.numericalUpdate()
      },

    }
  }
</script>

<style scoped>
  .dialog_link_info {
    margin-top: 10px;
    margin-left: 10px;
    position: absolute;
    width: 190px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    border-radius: 4px;
    text-align: center;
  }
</style>
