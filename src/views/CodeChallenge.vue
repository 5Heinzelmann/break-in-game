<template>
    <div class="code-challenge ">
        <div class="task">{{ task }}</div>
            
            <div class="controls">
            <q-btn
                    class= "btn"
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

        <codemirror v-model="input" :options="cmOptions"/>

        <div class="controls">
            <q-btn
                    class= "btn"
                    label="RESET"
            />

            <q-input
                square
                filled
                bg-color="white"
                color="grey"
                label="Please set your input"
            />

            <q-btn 
                :loading="loadingExecute"
                class= "btn"
                @click="executeCode()"
            >
                {{ buttonLabel }}
                <template v-slot:loading>
                    {{ buttonLabel }}
                    <q-spinner-gears/>
                </template>
            </q-btn>

        </div>        

        <div class="output">{{ output }}</div>

        <q-btn
                class= "btn"
                label="Final Test (on Success -> Show Scorecard)"
        />

        <router-link to="/score">Show Scoreboard</router-link>
    </div>
</template>

<script>
    import jsonChallenges from "../challenges/challenge.json";
    // import ImplementCode from '@/components/ImplementCode.vue';
    const axios = require("axios");
    import "codemirror/theme/base16-dark.css";

    export default {
        name: "CodeChallenge",
        // components: {
        //     ImplementCode
        // },
        data() {
            const langs = jsonChallenges.languages.map(lang => ({
                label: lang.name,
                value: lang.id,
                example: lang.example
            }));

            return {
                output: 'No Output yet',
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
                loadingExecute: false,
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
            },
            task() {
                const allTasks = jsonChallenges.challenges.map(item => {
                    return item.task;
                });
                return allTasks.length !== 0 ? allTasks[0] : null;
            }
        },
        methods: {
            executeCode() {
                if (this.finished) {
                    this.finished = false;
                    this.buttonLabel = "Wait...";
                    this.loadingExecute = true;
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
                    this.loadingExecute = false;
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
