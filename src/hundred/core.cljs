(ns hundred.core
  (:require [clojure.browser.repl :as repl]
            [goog.events :as events]
            [om.core :as om]
            [om.dom :as dom]
            [om-bootstrap.button :as b]
            [om-tools.dom :as d]
            [om-tools.dom :include-macros true])
  (:import [goog History]
           [goog.history EventType]))

;; (defonce conn
;;   (repl/connect "http://localhost:9000/repl"))

(enable-console-print!)

(def app-state
  (atom
    (let [typical-answers [{:text "Most perfect" :value 80}
                           {:text "not bad" :value 60}
                           {:text "ok, it's fine" :value 50}
                           {:text "you could do better" :value 25}
                           {:text "nobody thinks like this" :value 5}]]
      {:current-round-id    0
       :current-question-id 0
       :opened-questions    []
       :left-team           0
       :right-team          0
       :questions           [[{:question "round1-wtf1?" :answers typical-answers}
                              {:question "round1-wtf2?" :answers typical-answers}
                              {:question "round1-wtf3?" :answers typical-answers}
                              {:question "round1-wtf4?" :answers typical-answers}
                              {:question "round1-wtf5?" :answers typical-answers}]
                             [{:question "round2-wtf1?" :answers typical-answers}
                              {:question "round2-wtf2?" :answers typical-answers}
                              {:question "round2-wtf3?" :answers typical-answers}
                              {:question "round2-wtf4?" :answers typical-answers}
                              {:question "round2-wtf5?" :answers typical-answers}]
                             [{:question "round3-wtf1?" :answers typical-answers}
                              {:question "round3-wtf2?" :answers typical-answers}
                              {:question "round3-wtf3?" :answers typical-answers}
                              {:question "round3-wtf4?" :answers typical-answers}
                              {:question "round3-wtf5?" :answers typical-answers}]]})))

(def history (History.))

(events/listen history EventType.NAVIGATE
               (fn [e] (println (.-token e))))

(.setEnabled history true)

(defn answer-view [data owner]
  (reify
    om/IInitState
    (init-state [_] {:opened false})
    om/IRenderState
    (render-state [_ {:keys [opened]}]
      (b/button
        {:bs-style "primary" :bs-size "large" :block? true :style {:background-color "#ffb3b3"}
                   :on-click (fn [e] (om/set-state! owner :opened true))}
        (if opened (str (:text data) " - " (:value data)) (inc (:id data)))))))

(defn content [{:keys [questions]} owner]
  (reify
    om/IInitState
    (init-state [_]
      {:current-round-id    0
       :current-question-id 0})
    om/IRenderState
    (render-state [_ {:keys [current-round-id current-question-id]}]
      (let [round-questions (get questions current-round-id)
            question (get round-questions current-question-id)]
        (apply d/div {:class "well"
                      :style {:max-width 300
                              :margin    "0 auto 10px"}}
               (dom/h1 nil (:question question))
               (om/build-all answer-view (map-indexed (fn [id answer] (conj answer {:id id})) (:answers question))))))))

(om/root content app-state
         {:target (.getElementById js/document "content")})

(println "Hello world!")
