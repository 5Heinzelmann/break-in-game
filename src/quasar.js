import Vue from "vue";

import "./styles/quasar.sass";
import "@quasar/extras/material-icons/material-icons.css";
import {Quasar, QSelect} from "quasar";

Vue.use(Quasar, {
  config: {
    brand: {
      primary: '#a9218e',
      secondary: '#26A69A',
      accent: '#9C27B0',

      dark: '#1d1d1d',

      positive: '#21BA45',
      negative: '#C10015',
      info: '#31CCEC',
      warning: '#F2C037'
    }
  },
  components: {
    QSelect
  },
  directives: {
    /* not needed if importStrategy is not 'manual' */
  },
  plugins: {}
});
