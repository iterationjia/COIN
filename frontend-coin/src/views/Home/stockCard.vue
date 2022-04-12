<template>
  <div>
    <div>
      <!--    <el-row>-->
      <!--      <el-col :span="4" v-for="(o, index) in recommendList.length" :key="o"-->
      <!--              class="col" :offset="0">-->
      <!--        <el-popover-->
      <!--          placement="top-start"-->
      <!--          title=""-->
      <!--          width="200"-->
      <!--          trigger="hover"-->
      <!--          :open-delay="500">-->
      <!--          <span>{{getContent(index)}}-->
      <!--              <el-button class="el-icon-magic-stick" size="mini" style="float: right; margin-right: 20px"-->
      <!--                         @click="favourStock(index)"></el-button>-->
      <!--          </span>-->
      <!--          <el-card :body-style="{ padding: '0px' }" class="cards" slot="reference">-->
      <!--            {{recommendList[index][1]}}-->
      <!--          </el-card>-->
      <!--        </el-popover>-->
      <!--      </el-col>-->
      <!--    </el-row>-->
    </div>
    <div>
      <el-drawer
        :visible.sync="stockCardVisible"
        :before-close="handleStockCardClose"
        size="40%">
        <img class="recommend_img"
             src="https://lililizi.oss-cn-beijing.aliyuncs.com/a_%E8%BD%AF%E5%B7%A53/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20210609155511.png">
        <el-col :span="20" v-for="(o, index) in recommendList.length" :key="o" class="col" :offset="0">
          <el-popover
            placement="top-start"
            title=""
            width="200"
            trigger="hover"
            :open-delay="500">
            <el-button class="el-icon-magic-stick" size="mini" style="float: right; margin-right: 20px"
                       @click="favourStock(index)"></el-button>

            <el-card :body-style="{ padding: '0px' }" class="cards" slot="reference">
              {{recommendList[index][1]}}
            </el-card>
          </el-popover>
        </el-col>
      </el-drawer>
    </div>
  </div>
</template>

<script>
  import {mapActions, mapMutations, mapGetters} from 'vuex'

  export default {
    name: "stockCard",
    async mounted() {
      await this.getRecommendList()
      console.log(this.recommendList)
    },
    data() {
      return {
        currentGraph: 0,
        recommendList: [['1231313123','lyxnb','20.13121321','23.123131321','',''],
          ['1231313123','lyxnb','20.13121321','23.123131321','',''],['1231313123','lyxnb','20.13121321','23.123131321','',''],
          ['1231313123','lyxnb','20.13121321','23.123131321','',''],['1231313123','lyxnb','20.13121321','23.123131321','',''],
          ['1231313123','lyxnb','20.13121321','23.123131321','',''],['1231313123','lyxnb','20.13121321','23.123131321','',''],
          ['1231313123','lyxnb','20.13121321','23.123131321','',''],['1231313123','lyxnb','20.13121321','23.123131321','',''],
          ['1231313123','lyxnb','20.13121321','23.123131321','',''],['1231313123','lyxnb','20.13121321','23.123131321','',''],
          ['1231313123','lyxnb','20.13121321','23.123131321','',''],['1231313123','lyxnb','20.13121321','23.123131321','','']]
      }
    },
    computed: {
      ...mapGetters([
        // 'recommendList',
        'favourStockName',
        'stockCardVisible',
      ])
    },
    methods: {
      ...mapMutations([
        'set_favourStockName',
        'set_stockCardVisible'
      ]),

      ...mapActions([
        'getRecommendList',
        'postUserFavourStock',
      ]),

      favourStock(index) {
        this.$message.success("会在之后的推荐中增加" + this.recommendList[index][1] + "的权重")
        this.set_favourStockName(this.recommendList[index][1])
        this.postUserFavourStock()
      },

      handleStockCardClose() {
        this.set_stockCardVisible(false)
      }

    },
  }
</script>

<style scoped>
  .recommend_img {
    width: 15%;
    position: center;
    margin-bottom: 10px;
    margin-right: 6%;
  }

  .cards {
    width: 100%;
    height: 100px;
    margin-bottom: 5px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
    font-size: 20px;
    margin-left: 6%;
    margin-right: 4%;
  }

  .cards:hover {
    opacity: 0.8;
    border-bottom: 1px solid #000;
  }

  .el-drawer{
    overflow: scroll;
  }

</style>
