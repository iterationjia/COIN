import Vue from 'vue'
import Perform from "@/components/Perform";

describe('Perform.vue', () => {
  it('should render correct contents', () => {
    const Constructor = Vue.extend(Perform)
    const vm = new Constructor().$mount()
    expect(vm.$el.querySelector('.hello h1').textContent)
      .toEqual('Welcome to Our AC Project')
  })
})
