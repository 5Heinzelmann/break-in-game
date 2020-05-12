<template>
    <div class="code-challenge">
        <div class="task">{{ task }}</div>

        <ImplementCode/>

        <div class="output">{{ output }}</div>
        <router-link to="/score">Show Scoreboard</router-link>
    </div>
</template>

<script>
    import jsonChallenges from "../challenges/challenge.json";
    import ImplementCode from '@/components/ImplementCode.vue';

    export default {
        name: "CodeChallenge",
        components: {
            ImplementCode
        },
        mounted() {
            this.loadExample();
        },
        computed: {
            task() {
                const allTasks = jsonChallenges.challenges.map(item => {
                    return item.task;
                });
                return allTasks.length !== 0 ? allTasks[0] : null;
            }
        }
    };
</script>

<style lang="scss">

    .code-challenge {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        height: 100vh;
        width: 100vw;

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
