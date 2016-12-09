(ns lambda-io.slides
  (:require
    [re-com.core :as re-com]
    [re-frame.core :as re-frame]
    [reagent.core :as reagent]))

(def initial-db {:current-slide 0})

(defn slide-1 []
  [:main
   [:h1 "clojurescript"]
   [:ul
    [:li "clojure no browser"]
    [:li "tipos imutáveis"]
    [:li "usa o compilador do google closure por baixo dos panos"]
    [:li "assim como o clojure e o java, o clojurescript e o javascript possuem interop muito boa"]]])

(defn timer-component []
  (let [seconds-elapsed (reagent/atom 0)]
    (fn []
      (js/setTimeout #(swap! seconds-elapsed inc) 1000)
      [:span
       "Seconds Elapsed: " @seconds-elapsed])))

(defn slide-2 []
  [:main
   [:h1 "reagent"]
   [:ul
    [:li "wrapper do ReactJS pra clojurescript"]
    [:li "ao invés de html, o DOM dos componentes é definido em hiccup"]
    [:li "sua base é o reagent/atom"]
    [:li "componente exemplo:"
     [:pre [timer-component]]]]])

(defn slide-3 []
  [:div
   [:h1 "re-frame"]
   [:ul
    [:li "o reagent define a view, o re-frame dá estrutura"]
    [:li "fluxo unidirecional dos dados"]
    [:img {:src "img/reframe.svg" :width "600px" :height "350px"}]]])

(defn slide-4 []
  [:div
   [:h1 "ferramental"]
   [:ul
    [:li "leiningen (task runner)"]
    [:li "cljsbuild"]
    [:li "figwheel (hot-reload + repl)"]
    [:li "re-frisk (ver o db em tempo real)"]
    [:li "re-com (biblioteca de UI)"]]])

(defn slide-5 []
  [:div
   [:h1 "então..."]
   [:ul
    [:li "o re-frame permite que um app cresça com estrutura bem definida"]
    [:li "tem várias ferramentas interessantes pipocando a cada momento (só passamos pela superfície)"]
    [:li "são dados puros transitando, o que ajuda muito a entender o que está acontecendo em cada ponto"]]])

(def slides [[slide-1]
             [slide-2]
             [slide-3]
             [slide-4]
             [slide-5]])







;;; subscriptions

(re-frame/reg-sub :slides/current (fn [db] (get-in db [:slides :current-slide])))
(re-frame/reg-sub :slides/progress
  :<- [:slides/current]
  (fn [current] (-> (inc current)
                    (/ (count slides))
                    (* 100)
                    int)))









;;; handlers

(re-frame/reg-event-db :slides/next
  (fn [db]
    (update-in db [:slides :current-slide] inc)))

(re-frame/reg-event-db :slides/prev
  (fn [db]
    (update-in db [:slides :current-slide] dec)))











;;; views

(defn slide [current-index]
  (get slides current-index))

(defn next-prev [current-index]
  [re-com/h-box
   :justify :end
   :gap "20px"
   :children [[re-com/md-circle-icon-button
               :md-icon-name "zmdi-arrow-left"
               :size :larger
               :disabled? (= current-index 0)
               :on-click #(re-frame/dispatch [:slides/prev])]
              [re-com/md-circle-icon-button
               :md-icon-name "zmdi-arrow-right"
               :size :larger
               :disabled? (= current-index (dec (count slides)))
               :on-click #(re-frame/dispatch [:slides/next])]]])

(defn controls [current-index]
  [re-com/v-box
   :style {:position :fixed
           :bottom "1%"
           :right "1%"}
   :gap "10px"
   :children [[next-prev current-index]
              [re-com/progress-bar :model (re-frame/subscribe [:slides/progress])]]])

(defn view []
  (let [current-slide* (re-frame/subscribe [:slides/current])]
    (fn view-render []
      [:main
       [slide @current-slide*]
       [controls @current-slide*]])))
