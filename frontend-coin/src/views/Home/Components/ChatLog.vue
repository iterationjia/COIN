<template>
  <div class="box" >
    <!--      <el-button @click="addHello">添加对话</el-button>-->
    <!--      <VueChatUI ref="chat"></VueChatUI>-->
    <VueChat class="vuechat" :title="'问答平台'" :initial-feed="feed" @newOwnMessage="onNewOwnMessage"
             :new-message="message" ref="chatLog"></VueChat>

  </div>
</template>

<script>
  import VueChat from 'basic-vue-chat'
  import {mapMutations, mapActions, mapGetters} from 'vuex'

  export default {
    components: {
      VueChat
    },
    computed:{
      ...mapGetters([
        'chatBotAnswer'
      ])
    },
    data() {
      return {
        drawVisible: true,
        date: '',
        feed: [
          {
            id: 1,
            author: 'Siri',
            contents: '您好，我是您的专属金融智能助理。您有什么需要我解答的吗？您可以这样来问我：“南京高科股份有限公司的详细信息” ' +
              '“有色金属冶炼和压延加工业的公司有哪些？”“王艳相关的公司”“南京高科的公司的ceo是谁”',
            date: '',
          },
        ],
        message: {}
      }
    },
    methods: {
      ...mapMutations([
        'set_chatBotRequest',
        'set_chatBotAnswer',
      ]),

      ...mapActions([
        'getChatBotAnswer'
      ]),

      async onNewOwnMessage(message) {
        await this.set_chatBotRequest(message)
        await this.getChatBotAnswer()
        console.log(this.chatBotAnswer)
        this.message = {
          id: 1,
          author: 'Siri',
          contents: this.chatBotAnswer,
          date: '',
        }
      },
    }
  }
</script>

<style scoped>
.vuechat >>> .window {
  height: 1000px;
}
</style>
