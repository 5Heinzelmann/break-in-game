<template lang="html">
  <div class="code-challenge ">
    <div
        class="task">
      {{ task }}
    </div>

    <div class="controls">
      <q-btn
          class="btn"
          label="Hints for time penalty"
      />

      <q-select
          v-model="selectedLang"
          :options="langOptions"
          label="Language"
          color="grey"
          filled
          square
          bg-color="white"
          class="lang-select"
          @input="loadExample()"
      />
    </div>

    <codemirror v-model="input" :options="cmOptions" ref="cmEditor" />

    <div class="controls">
      <q-btn
          class="btn"
          label="RESET"
          @click="loadExample()"
      />

      <q-input
          square
          filled
          bg-color="white"
          color="grey"
          v-model="numberAsInput"
          type="number"
          label="Number as input"
      />

      <q-btn
          :loading="loadingExecute"
          class="btn"
          @click="executeCode()"
      >
        {{ buttonLabel }}
        <template v-slot:loading>
          {{ buttonLabel }}
          <q-spinner-gears/>
        </template>
      </q-btn>

    </div>

    <div
        class="output">
      {{ output }}
    </div>

    <q-btn
        class="btn"
        label="Final Test (on Success -> Show Scorecard)"
    />

    <router-link to="/score">Show Scoreboard</router-link>
  </div>
</template>

<script>
import jsonChallenges from "../challenges/challenge.json";
import "codemirror/theme/base16-dark.css";
import "codemirror/mode/javascript/javascript.js";
import "codemirror/mode/clike/clike.js";
import "codemirror-formatting/formatting.js";

const axios = require("axios");
const REPLACE_WITH_INPUT = "REPLACE_WITH_INPUT";

export default {
  name: "CodeChallenge",
  data() {
    const langs = jsonChallenges.languages.map(lang => ({
      label: lang.name,
      value: lang.id,
      example: lang.example
    }));

    return {
      numberAsInput: '3',
      output: 'No Output yet',
      // uses port 80 as default. if this wont work use: http://localhost:3000/submissions/ and
      // make sure to update docker-compose.yml to  ports: - "3000:3000"
      url: "http://localhost/submissions/",
      interval: null,
      input: "",
      finished: true,
      buttonLabel: "Execute",
      loadingExecute: false,
      selectedLang: langs.length > 0 ? langs[0] : null,
      langOptions: langs,
      cmOptions: {
        tabSize: 4,
        styleActiveLine: true,
        styleSelectedText: true,
        mode: "text/x-java",
        theme: "base16-dark",
        lineNumbers: true,
        line: true
      }
    };
  },
  mounted() {
    this.loadExample();
  },
  computed: {
    selectedLanguageId() {
      return this.selectedLang ? this.selectedLang.value : null;
    },
    task() {
      const allTasks = jsonChallenges.challenges.map(item => {
        return item.task;
      });
      return allTasks.length !== 0 ? allTasks[0] : null;
    },
    codemirror() {
      return this.$refs.cmEditor.codemirror;
    }
  },
  methods: {
    executeCode() {
      this.autoFormatCode();
      if (this.finished) {
        this.finished = false;
        this.buttonLabel = "Wait...";
        this.loadingExecute = true;

        const prefix = jsonChallenges.challenges[0].executionCode[this.selectedLang.label.toLowerCase()].prefix;
        const postfix = jsonChallenges.challenges[0].executionCode[this.selectedLang.label.toLowerCase()].postfix;
        let executionCode = prefix + this.input + postfix;
        executionCode = executionCode.replace(REPLACE_WITH_INPUT, this.numberAsInput);

        const data = {
          source_code: executionCode,
          language_id: this.selectedLanguageId
        };

        axios.post(this.url, data)
            .then(resp => {
              const token = resp.data.token;

              this.interval = setInterval(() => {
                this.fetchResult(token);
              }, 500);
            })
            .catch((e) => {
              console.log(e);
              this.output = e;
              this.resetForm();
            });
      }
    },
    resetForm: function () {
      this.finished = true;
      this.buttonLabel = "Execute";
      this.loadingExecute = false;
    },
    fetchResult(token) {
      axios.get(this.url + token)
          .then(resp => {
            console.log(resp);

            if (resp.data.status.id > 2) {
              clearInterval(this.interval);
            }

            this.output = resp.data.stdout

            if(!this.output) {
              this.output = resp.data.stderr ? resp.data.stderr : resp.data.compile_output
            }

            this.resetForm();
          })
          .catch((e) => {
            this.output = e;
            this.resetForm();
          });
    },
    loadExample() {
        const selectedLang = this.selectedLang.label.toLowerCase();
        this.input = jsonChallenges.challenges[0].initialCode[selectedLang];
        this.cmOptions.mode = jsonChallenges.challenges[0].syntaxHighlight[selectedLang];
        setTimeout(() => {
          this.autoFormatCode();
        }, 200);
    },
    autoFormatCode() {
      var totalLines = this.codemirror.lineCount();
      var totalChars = this.codemirror.getTextArea().value.length;
      this.codemirror.autoFormatRange({ line: 0, ch: 0 }, { line: totalLines, ch: totalChars });
    }
  }
};
</script>

<style lang="scss">

.code-challenge {
  margin: auto;
  padding: 30px;
  height: 100vh;
  width: 50%;

  .output {
    color: #ffffff;
    background-color: #09141c;
    font-family: monospace;
    font-weight: bold;
    padding: 16px;
    font-size: 16px;
  }

  .controls {
    display: flex;
    justify-content: space-between;
    margin: 10px 0;

    .lang-select {
      font-size: 20px;
      width: 150px;
    }
  }

  .input {
    width: 700px;
    height: 300px;
    border: 0;
    padding: 8px 16px;
    line-height: 35px;
  }

  .btn {
    display: flex;
    background-color: #a9218e;
    font-weight: bold;
    color: #f4f4f4;
    cursor: pointer;
    justify-content: center;
    align-items: center;
    padding: 10px;
    margin: 12px 0 14px 0;
  }

  .btn:hover {
    background-color: #d329b2;
  }

  .task {
    color: #ffffff;
    background-color: #09141c;
    font-family: monospace;
    font-weight: bold;
    text-align: center;
    padding: 16px;
    font-size: 16px;
    width: 100%;
  }
}
</style>
