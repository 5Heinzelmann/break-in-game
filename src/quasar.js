import Vue from "vue";

import "./styles/quasar.sass";
import "@quasar/extras/material-icons/material-icons.css";
import { Quasar, QSelect } from "quasar";

Vue.use(Quasar, {
  config: {},
  components: {
    QSelect
  },
  directives: {
    /* not needed if importStrategy is not 'manual' */
  },
  plugins: {}
});
