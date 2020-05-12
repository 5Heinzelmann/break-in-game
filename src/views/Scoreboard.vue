<template>
    <div class="board">
        <div>
            <q-input v-model="name" label="Your name"/>
            <q-input type="number" v-model="score" label="Your score"/>

            <q-btn color="primary" :disable="name === '' || score < 0" @click="submitScore()">Submit</q-btn>
        </div>
        <q-table
                title="Scoreboard"
                :data="scores"
                :columns="columns"
                row-key="id"
                :pagination.sync="pagination"
                hide-bottom
        />
    </div>
</template>

<script>

    export default {
        name: "ScoreBoard",
        data() {
            return {
                name: '',
                score: null,
                scores: [],
                columns: [
                    {name: 'name', label: 'Username', field: 'name', align: 'left'},
                    {name: 'score', label: 'Score', field: 'score'},
                ],
                pagination: {
                    page: 1,
                    rowsPerPage: 0,
                    sortBy: 'score',
                    descending: true
                }
            }
        },
        mounted() {
            if (localStorage.scores) {
                this.scores = JSON.parse(localStorage.scores)
            }
        },
        computed: {},
        methods: {
            submitScore() {
                if (this.name !== '' && this.score >= 0 && this.score < Number.MAX_SAFE_INTEGER) {
                    this.scores.push({
                        id: this.uuidv4(),
                        name: this.name,
                        score: this.score
                    });
                    this.scores = this.scores.sort((a, b) => {
                        return a.score < b.score
                    });
                    localStorage.scores = JSON.stringify(this.scores);
                    this.name = '';
                    this.score = '';
                }
            },
            uuidv4() {
                return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                    var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
                    return v.toString(16);
                });
            }
        },
    };
</script>

<style lang="scss">
    .board {
        display: flex;
        justify-content: center;
    }
</style>
