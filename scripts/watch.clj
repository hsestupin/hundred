(require '[cljs.build.api :as b])

(b/watch "src"
  {:main 'hundred.core
   :output-to "out/hundred.js"
   :output-dir "out"})
