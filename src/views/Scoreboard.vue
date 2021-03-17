<template lang="html">
    <div class="board">
        <h2>Well done!</h2>
        <q-dialog v-model="showScoreForm" persistent>
            <q-card>
                <q-card-section>
                    <q-input v-model="name" label="Your name"/>
                    <div style="display: flex">
                        <q-input type="number" v-model.number="minutes" label="Minutes"/>
                        <q-input type="number" v-model.number="seconds" label="Seoncds"/>
                    </div>
                </q-card-section>
                <q-card-actions align="right">
                    <q-btn @click="showScoreForm = false">Cancel</q-btn>
                    <q-btn color="primary" :disable="name === '' || minutes < -1 || seconds < 0" @click="submitScore()">
                        Submit
                    </q-btn>
                </q-card-actions>
            </q-card>
        </q-dialog>
        <q-table
            title="Scoreboard"
            :data="scores"
            :columns="columns"
            dark
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
            showScoreForm: true,
            name: '',
            minutes: 0,
            seconds: 0,
            scores: [],
            columns: [
                {name: 'name', label: 'Username', field: 'name', align: 'left'},
                {name: 'time', label: 'Time', field: 'time', align: 'left'},
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
            this.scores = JSON.parse(localStorage.scores);
            this.sortScores();
        }
    },
    computed: {},
    methods: {
        // TODO round score values to abs
        // limit seconds to 59 max
        // show error on input
        submitScore() {
            const score = -0.6 * (this.minutes * 60 + this.seconds) + 1035;
            this.scores.push({
                id: this.uuidv4(),
                name: this.name,
                time: `${this.minutes}:${this.seconds}`,
                score: Math.round(score)
            });
            this.sortScores();
            localStorage.scores = JSON.stringify(this.scores);
            this.name = '';
            this.minutes = 0;
            this.seconds = 0;
            this.showScoreForm = false;
        },
        uuidv4() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            });
        },
        sortScores() {
            this.scores = this.scores.sort((a, b) => {
                return a.score < b.score
            });

        }
    },
};
</script>

<style lang="scss">
.board {
    display: flex;
    flex-direction: column;
    align-items: center;

    h2 {
        color: #00B2BB;
        background-color: black;
        font-weight: bold;
        padding: 8px
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
}
</style>
