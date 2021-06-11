<template lang="html">
  <div>
    <div class="code-challenge">
      <div
          class="task">
        {{ task }}
      </div>

      <div class="controls">
        <q-btn
            class="btn"
            label="Auto Format"
            @click="autoFormatCode()"
        />

        <q-select
            v-model="selectedLanguage"
            :options="langOptions"
            bg-color="white"
            class="lang-select"
            color="grey"
            filled
            label="Language"
            square
            @input="checkReset = true"
        />
      </div>

      <codemirror ref="cmEditor" v-model="input" :options="cmOptions" @changes="saveToStorage()"/>

      <div class="controls">
        <q-btn
            class="btn"
            label="Reset"
            @click="checkReset = true"
        />

        <q-input
            v-model="numberAsInput"
            bg-color="white"
            color="grey"
            class="inputNumber"
            filled
            label="inputNumber"
            square
            type="number"
            min="1"
            max="20"
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

      <div class="controls">
        <q-btn
            class="btn"
            label="Complete Challenge"
            @click="runTests()"
        />
      </div>

      <q-dialog v-model="checkReset" persistent>
        <q-card>
          <q-card-section class="row items-center">
            <q-icon name="warning" class="text-red" style="font-size: 2rem;"/>
            <span class="q-ml-sm">Your code will be reseted. Are you sure?</span>
          </q-card-section>

          <q-card-actions align="right">
            <q-btn label="No" v-close-popup/>
            <q-btn label="Yes reset my code" color="accent" @click="loadExample()" v-close-popup/>
          </q-card-actions>
        </q-card>
      </q-dialog>
    </div>
    <div class="error-section">
      <div class="error-message">
        {{ error }}
      </div>
    </div>
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
    const languages = jsonChallenges.languages.map(lang => ({
      label: lang.name,
      value: lang.id,
      example: lang.example
    }));

    return {
      numberAsInput: '3',
      output: 'No Output yet',
      error: 'The size smaller 700px is not usable and therefore supported.',
      // uses port 80 as default. if this wont work use: http://localhost:3000/submissions/ and
      // make sure to update docker-compose.yml to  ports: - "3000:3000"
      url: "http://localhost/submissions/?wait=true",
      input: "",
      checkReset: false,
      finished: true,
      buttonLabel: "Execute",
      loadingExecute: false,
      selectedLanguage: languages.length > 0 ? languages[0] : null,
      langOptions: languages,
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
    const storage = window.localStorage;
    const selectedLangId = localStorage.getItem('selectedLanguageId');
    const code = localStorage.getItem('code');
    const found = this.langOptions.filter(l => l.value.toString() === selectedLangId);
    if (found.length > 0) {
      this.selectedLanguage = found[0];
    }
    if (code) {
      const selectedLanguage = this.selectedLanguage.label.toLowerCase();
      this.input = code;
      this.cmOptions.mode = jsonChallenges.challenges[0].syntaxHighlight[selectedLanguage];
    } else {
      this.loadExample();
    }
  },
  computed: {
    selectedLanguageId() {
      return this.selectedLanguage ? this.selectedLanguage.value : null;
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
      return new Promise((resolve, reject) => {
        this.autoFormatCode();
        if (this.finished) {
          this.finished = false;
          this.buttonLabel = "Wait...";
          this.loadingExecute = true;

          const prefix = jsonChallenges.challenges[0].executionCode[this.selectedLanguage.label.toLowerCase()].prefix;
          const postfix = jsonChallenges.challenges[0].executionCode[this.selectedLanguage.label.toLowerCase()].postfix;
          let executionCode = prefix + this.input + postfix;
          executionCode = executionCode.replace(REPLACE_WITH_INPUT, this.numberAsInput);

          const data = {
            source_code: executionCode,
            language_id: this.selectedLanguageId,
          };

          axios.post(this.url, data)
              .then(resp => {
                this.output = resp.data.stdout
                if (!this.output) {
                  if (resp.data.stderr) {
                    this.output = resp.data.stderr;
                  } else {
                    this.output = resp.data.compile_output;
                    const languageFileEnding = '.' + this.selectedLanguage.label.toLowerCase();
                    const isErrorMessage = this.output.includes(languageFileEnding);
                    if (isErrorMessage) {
                      const partWithWrongLineNumber = this.output.split(languageFileEnding)[1];
                      const wrongLineNumber = +partWithWrongLineNumber.split(":")[1];
                      const countLineBreaks = (prefix.match(/\n/g) || []).length;
                      const correctedLineNumber = +wrongLineNumber - countLineBreaks;
                      this.output = this.output.replace(`:${wrongLineNumber}:`, `:${correctedLineNumber}:`)
                    }
                  }
                }
                if (!isNaN(this.output)) {
                  this.formatOutput();
                }
                this.resetForm();
                resolve(this.output);
              })
              .catch((e) => {
                console.error(e);
                this.output = e;
                this.resetForm();
                reject();
              });
        }
      });
    },
    resetForm: function () {
      this.finished = true;
      this.buttonLabel = "Execute";
      this.loadingExecute = false;
    },
    loadExample() {
      const selectedLang = this.selectedLanguage.label.toLowerCase();
      this.input = jsonChallenges.challenges[0].initialCode[selectedLang];
      this.cmOptions.mode = jsonChallenges.challenges[0].syntaxHighlight[selectedLang];
      setTimeout(() => {
        this.autoFormatCode();
      }, 200);
    },
    saveToStorage() {
      const storage = window.localStorage;
      localStorage.setItem('selectedLanguageId', this.selectedLanguageId);
      localStorage.setItem('code', this.input);
    },
    autoFormatCode() {
      var totalLines = this.codemirror.lineCount();
      var totalChars = this.codemirror.getTextArea().value.length;
      this.codemirror.autoFormatRange({line: 0, ch: 0}, {line: totalLines, ch: totalChars});
      this.codemirror.setSelection({line: 0, ch: 0}, {line: 0, ch: 0});
    },
    formatOutput() {
      const numberStr = this.output.replace("\n", "");
      if (numberStr.length > 3) {
        const mod = numberStr.length % 3;
        let output = (mod > 0 ? (numberStr.substring(0, mod)) : '');
        for (let i = 0; i < Math.floor(numberStr.length / 3); i++) {
          if ((mod === 0) && (i === 0))
            output += numberStr.substring(mod + 3 * i, mod + 3 * i + 3);
          else
              // hier wird das Trennzeichen festgelegt mit '.'
            output += '.' + numberStr.substring(mod + 3 * i, mod + 3 * i + 3);
        }
        this.output = output;
      }
    },
    runTests: function () {
      this.numberAsInput = jsonChallenges.challenges[0].testInputs[0];
      let expectedOutput = jsonChallenges.challenges[0].expectedOutputs[0];
      let answersAreCorrect = [];

      this.executeCode().then(answer => {
        let actual = answer.toString().replaceAll(".", "").trim();
        if (+actual === +expectedOutput) {
          answersAreCorrect.push({correct: true});
        } else {
          answersAreCorrect.push({correct: false, input: this.numberAsInput, expectedOutput});
        }

        this.numberAsInput = jsonChallenges.challenges[0].testInputs[1];
        expectedOutput = jsonChallenges.challenges[0].expectedOutputs[1];

        this.executeCode().then(answer => {
          actual = answer.toString().replaceAll(".", "").trim();
          if (+actual === +expectedOutput) {
            answersAreCorrect.push({correct: true});
          } else {
            answersAreCorrect.push({correct: false, input: this.numberAsInput, expectedOutput});
          }

          this.numberAsInput = jsonChallenges.challenges[0].testInputs[2];
          expectedOutput = jsonChallenges.challenges[0].expectedOutputs[2];

          // TODO please loop this
          this.executeCode().then(answer => {
            actual = answer.toString().replaceAll(".", "").trim();
            if (+actual === +expectedOutput) {
              answersAreCorrect.push({correct: true});
            } else {
              answersAreCorrect.push({correct: false, input: this.numberAsInput, expectedOutput});
            }

            console.log(answersAreCorrect);
            if (!answersAreCorrect.filter(a => !a.correct).length) {
              this.$router.push('/score')
            } else {
              alert('Oh no! Your solution is not yet correct.')
            }
          })
        })
      });
    }
  }
};
</script>

<style lang="scss">

.code-challenge {
  margin: auto;
  padding: 30px;
  height: 100vh;
  width: 100%;
  max-width: 1000px;

  .inputNumber {
    width: 20%;
  }

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

  .CodeMirror {
    height: auto;
    max-height: 400px;
  }
}

.error-section {
  display: none;
}

@media only screen and (max-width: 700px) {
  /* for tablets and smaller: */
  .code-challenge {
    display: none;
  }
  .error-section {
    display: block;
    padding: 80px;

    .error-message {
      color: #ffffff;
      background-color: #09141c;
      font-family: monospace;
      font-weight: bold;
      padding: 16px;
      font-size: 16px;
    }
  }
}

</style>
