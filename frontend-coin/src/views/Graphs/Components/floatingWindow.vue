<template>
  <div class="box" id="movable">

  </div>

</template>
<script>
  import {mapGetters, mapMutations, mapActions} from 'vuex'

  export default {
    name: 'floatingWindow',
    data() {
      return {
        diffY: 0,
        diffX: 0,
        elmHeight: 0,
        elmWidth: 0,
        containerHeight: 0,
        containerWidth: 0,
        isMouseDown: false
      };
    },
    computed: {
      ...mapGetters([
        'graphMovable'
      ])
    },
    mounted() {
      document.getElementById('movable').addEventListener('mousedown', this.mouseDown)
      document.addEventListener('mousemove', this.mouseMove);
      document.addEventListener('mouseup', this.mouseUp);
    },

    methods: {
      ...mapMutations([
        'set_graphMovable'
      ]),

      mouseDown(e) {
        console.log('start move');
        this.isMouseDown = true;
        // get initial mousedown coordinated
        const mouseY = e.clientY;
        const mouseX = e.clientX;

        // get element top and left positions
        const elm = document.getElementById('movable');
        const elmY = elm.offsetTop - 90;
        const elmX = elm.offsetLeft - 10;

        // get elm dimensions
        this.elmWidth = elm.offsetWidth;
        this.elmHeight = elm.offsetHeight;

        // get container dimensions
        const container = elm.offsetParent;
        this.containerWidth = container.offsetWidth;
        this.containerHeight = container.offsetHeight;

        // get diff from (0,0) to mousedown point
        this.diffY = mouseY - elmY;
        this.diffX = mouseX - elmX;
      },

      mouseMove(e) {
        console.log('moving');
        if (!this.isMouseDown) return;
        const elm = document.getElementById('movable');
        // get new mouse coordinates
        const newMouseY = e.clientY;
        const newMouseX = e.clientX;

        // calc new top, left pos of elm
        let newElmTop = newMouseY - this.diffY,
          newElmLeft = newMouseX - this.diffX;

        // calc new bottom, right pos of elm
        let newElmBottom = newElmTop + this.elmHeight,
          newElmRight = newElmLeft + this.elmWidth;

        if ((newElmTop < 0) || (newElmLeft < 0) || (newElmTop + this.elmHeight > this.containerHeight)
          || (newElmLeft + this.elmWidth > this.containerWidth)) {
          // if elm is being dragged off top of the container...
          if (newElmTop < 0) {
            newElmTop = 0;
          }

          // if elm is being dragged off left of the container...
          if (newElmLeft < 0) {
            newElmLeft = 0;
          }

          // if elm is being dragged off bottom of the container...
          if (newElmBottom > this.containerHeight) {
            newElmTop = this.containerHeight - this.elmHeight;
          }

          // if elm is being dragged off right of the container...
          if (newElmRight > this.containerWidth) {
            newElmLeft = this.containerWidth - this.elmWidth;
          }
        }

        this.moveElm(elm, newElmTop, newElmLeft);
      },

      moveElm(elm, yPos, xPos) {
        elm.style.top = yPos + 'px';
        elm.style.left = xPos + 'px';
      },

      mouseUp() {
        console.log('end move');
        this.isMouseDown = false;
      }
    },
  }

</script>

<style scoped>
  @import url("https://fonts.googleapis.com/css?family=PT+Mono");

  body,
  html,
  .box {
    margin-top: 90px;
    margin-left: 10px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 20px;
    left: 90px;
    box-sizing: border-box;
    border: 2px solid currentColor;
    border-radius: 10px;
    color: #f4f1ee;
    background: ghostwhite;
    width: 150px;
    height: 150px;
    font-family: "PT Mono", monospace;
    font-size: 16px;
    cursor: pointer;
    user-select: none;
    text-align: center;
  }

  .box:hover {
    background-color: ghostwhite;
  }

  .box:active, .box:focus {
    background-color: ghostwhite;
    cursor: move;
    cursor: grab;
  }

  .box:active.fainted, .box:focus.fainted {
    background-color: rgba(255, 255, 255, 0.3);
  }

</style>

