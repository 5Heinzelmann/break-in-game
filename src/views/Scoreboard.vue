<template>
    <div class="board">
        <q-table
                square
                title="Scoreboard"
                :data="scores"
                :columns="columns"
                row-key="name"
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
            this.scores = [
                {name: 'Luke', score: 6000},
                {name: 'Bob', score: 8000},
                {name: 'Hieu', score: 9000},
                {name: 'Lisa', score: 7000},
            ]
        },
        computed: {},
        methods: {}
    };
</script>

<style lang="scss">
    .board {
        color: #ffffff;
        background-color: #262c31;
        display: flex;
        justify-content: center;
    }
</style>
