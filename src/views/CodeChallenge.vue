<template>
    <div class="code-challenge">
        <div class="grid-container"></div>
        <div class="grid-headerline"></div>
        <div class="task">{{ task }}</div>

        <ImplementCode/>

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

        .task {
            color: #ffffff;
            background-color: #09141c;
            font-family: monospace;
            font-weight: bold;
            padding: 16px;
            font-size: 16px;
        }
        
        .grid-container {
            display: grid;
            grid-template-columns: 20% 20% 20% 20% 20%;
            grid-template-rows: 15% 40% 15% 30%;            
        }
        
        .grid-headerline {
            align-self: stretch;
            // grid-column-start: 1;
            // grid-column-end: span col4-start;
            // grid-row-start: 2;
            // grid-row-end: span 2;
        }
    }

</style>
