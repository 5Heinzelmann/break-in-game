<template>
  <div id="app">
    <div class="column">
      <div class="col">
        <label for="lang"></label>
        <select id="lang" style="font-size: x-large" v-model="languageId">
          <option :value="63">Java Script</option>
          <option :value="62">Java</option>
        </select>
      </div>
      <div class="col">
        <div class="task">{{ task }}</div>
      </div>
      <div class="col">
        <button @click="loadExample()" style="font-size: x-large">
          Load example
        </button>
        <label>
          <codemirror v-model="input" :options="cmOptions" />
        </label>
      </div>
      <div class="col">
        <div @click="executeCode()" class="btn">
          <h1>{{ buttonLabel }}</h1>
        </div>
        <div class="output">{{ output }}</div>
      </div>
    </div>
  </div>
</template>

<script>
const axios = require("axios");
import jsonChallenges from "./challenges/challenge.json";
import 'codemirror/theme/base16-dark.css'

export default {
  name: "App",
  data() {
    return {
      url: "https://api.judge0.com/submissions/",
      interval: null,
      input: "",
      javaScriptExample: 'const x = "moin";\n' + "console.log(x, x);",
      javaExample:
        "class Main {\n" +
        "    public static void main(String[] args) {\n" +
        '        System.out.println("Hello, Java ;)");\n' +
        "    }\n" +
        "}",
      output: ">",
      // task: 'No Task.',
      finished: true,
      buttonLabel: "Execute",
      languageId: 62,
      cmOptions: {
        tabSize: 4,
        mode: "text/javascript",
        theme: "base16-dark",
        lineNumbers: true,
        line: true
      }
    };
  },
  mounted() {
    this.loadExample();
    this.loadTask();
  },
  computed: {
    task() {
      return jsonChallenges.challenges.map(item => {
        return item.task;
      });
    }
  },
  methods: {
    executeCode() {
      if (this.finished) {
        this.finished = false;
        this.buttonLabel = "Wait...";
        const data = {
          source_code: this.input,
          language_id: this.languageId
        };

        axios.post(this.url, data).then(resp => {
          const token = resp.data.token;

          this.interval = setInterval(() => {
            this.fetchResult(token);
          }, 500);
        });
      }
    },
    fetchResult(token) {
      axios.get(this.url + token).then(resp => {
        if (resp.data.status.id > 2) {
          clearInterval(this.interval);
        }
        this.output = resp.data.stdout
          ? "> " + resp.data.stdout
          : "> " + resp.data.status.description;
        this.finished = true;
        this.buttonLabel = "Execute";
      });
    },
    loadExample() {
      this.input =
        this.languageId === 62 ? this.javaExample : this.javaScriptExample;
    }
  }
};
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background-color: #262c31;

  .input {
    width: 700px;
    height: 300px;
    border: 0;
    padding: 8px 16px;
    line-height: 35px;
  }

  .btn {
    display: flex;
    background-color: #4db37c;
    color: #09141c;
    cursor: pointer;
    justify-content: center;
    align-items: center;
    margin: 12px 0 14px 0;
  }

  .btn:hover {
    background-color: #5acc8d;
  }

  .output {
    color: #4db37c;
    background-color: #09141c;
    font-family: monospace;
    font-weight: bold;
    padding: 16px;
    font-size: 16px;
  }

  .task {
    color: #ffffff;
    background-color: #09141c;
    font-family: monospace;
    font-weight: bold;
    padding: 16px;
    font-size: 16px;
  }
}
</style>
