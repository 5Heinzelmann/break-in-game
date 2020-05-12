<template>
  <div class="code-challenge">
    <div class="controls">
      <q-btn
        color="white"
        text-color="black"
        label="Load example"
        @click="loadExample()"
      />

      <q-select
        v-model="selectedLang"
        :options="langOptions"
        label="Language"
        filled
        square
        bg-color="white"
        class="lang-select"
      />
    </div>

    <codemirror v-model="input" :options="cmOptions" />

    <div @click="executeCode()" class="btn">
      <span class="text-h4"><b>{{ buttonLabel }}</b></span>
    </div>
  </div>
</template>

<script>
const axios = require("axios");
import jsonChallenges from "../challenges/challenge.json";
import "codemirror/theme/base16-dark.css";

export default {
  name: "ImplementCode",
  data() {
    const langs = jsonChallenges.languages.map(lang => ({
      label: lang.name,
      value: lang.id,
      example: lang.example
    }));

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
      finished: true,
      buttonLabel: "Execute",
      selectedLang: langs.length > 0 ? langs[0] : null,
      langOptions: langs,
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
  },
  computed: {
    selectedLanguageId() {
      return this.selectedLang ? this.selectedLang.value : null;
    }
  },
  methods: {
    executeCode() {
      if (this.finished) {
        this.finished = false;
        this.buttonLabel = "Wait...";
        const data = {
          source_code: this.input,
          language_id: this.selectedLanguageId
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
      this.input = this.selectedLang ? this.selectedLang.example : "";
    }
  }
};
</script>

<style lang="scss">
.code-challenge {
  min-width: 700px;
}

.controls {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;

  .lang-select {
    width: 200px;
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
  background-color: #4db37c;
  color: #09141c;
  cursor: pointer;
  justify-content: center;
  align-items: center;
  padding: 10px;
  margin: 12px 0 14px 0;
}

.btn:hover {
  background-color: #5acc8d;
}
</style>
