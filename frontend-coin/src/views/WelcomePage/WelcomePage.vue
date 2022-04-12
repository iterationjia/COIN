<template>
  <div @click="toHome" style="width:100%;height: 100%">
    <div class="container">
      <p>Welcome to our</p>
      <ul class="props">
        <li class="t">and</li>
        <li class="s">KG</li>
        <li class="fi">(^ω^)</li>
        <li class="f">ACoin</li>
        <li class="fo">Stock</li>
      </ul>
    </div>
  </div>
</template>

<script>
  export default {
    name: "WelcomePage",
    async mounted() {
      this.Move();
    },
    methods: {
      Move() {
        setInterval(function () {
          var first = $(".props li:first"),
            last = $(".props li:last");
          $(last).after(first);
        }, 2000);
      },
      toHome() {
        this.$router.push('/home')
      }
    }
  }
</script>

<style scoped>
  html, body {
    background: #2C3033;
    width: 100%;
    margin: 0;
    padding: 0;
    color: white;
    font-family: allura;
  }

  p {
    float: left;
    font-size: 72px;
    padding-top: 10px;
    padding-right: 5px;
    margin: 0;
  }

  .container {
    top: 10%;
    left: 43%;
    margin: -65px auto auto -240px;
    height: 130px;
    width: 100%;
    position: absolute;
  }

  .props {
    border-radius: 20px;
    width: 190px;
    height: 80px;
    position: relative;
    float: left;
    overflow: hidden;
    box-shadow: 0 0 4px 0 rgba(0, 0, 0, .4);
    margin: 16px;
  }

  .props:after {
    content: '';
    top: 0;
    left: 0;
    position: absolute;
    background: transparent;
    width: 100%;
    height: 100%;
    cursor: pointer;
  }

  .props li {
    transition: all .5s ease;
    position: absolute;
    top: 0;
    left: 0;
    background: white;
    width: 100%;
    height: 100%;
    font-family: arial;
    font-weight: bold;
    font-size: 4rem;
    color: white;
    text-align: center;
    padding-top: 2px;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, .5);
  }

  .props li:nth-of-type(5) {
    margin-top: -100px;
  }

  .props .f {
    background: #FF96CC;
  }

  .props .s {
    background: #96FF9C;
  }

  .props .t {
    background: #CCFFFE;
  }

  .props .fo {
    background: #EEFF70;
  }

  .props .fi {
    background: #FF4F64;
  }


</style>
