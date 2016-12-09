(ns lambda-io.github
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [re-com.core :as re-com]
    [reagent.core :as reagent]
    [ajax.core :as ajax]))


(def initial-db {:loading? false})

(defn result->internal
  "Converts a GitHub search result to what we're interested at"
  [result]
  {:url (:html_url result)
   :updated-at (:updated_at result)
   :language (:language result)})

(defn clojure? [result] (#{"Clojure"} (:language result)))

;; subscriptions

(re-frame/reg-sub :github/loading?
  (fn [db] (get-in db [:github :loading?])))

(re-frame/reg-sub :github/results
  (fn [db] (get-in db [:github :results])))

(re-frame/reg-sub :github/clj-results
  :<- [:github/results]
  (fn [results] (filter clojure? results)))













;; events

(re-frame/reg-event-fx :github/search
  (fn [{:keys [db]} [_event-name query]]
    {:db         (assoc-in db [:github :loading?] true)
     :http-xhrio {:method          :get
                  :uri             "https://api.github.com/search/repositories"
                  :params          {:q query}
                  :timeout         5000
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [:github/search-success query]
                  :on-failure      [:github/search-error query]}}))

(re-frame/reg-event-db :github/search-success
  (fn [db [_event-name _query response]]
    (-> db
        (assoc-in [:github :loading?] false)
        (assoc-in [:github :results] (map result->internal (:items response))))))

(re-frame/reg-event-db :github/search-error
  (fn [db [_event-name query _response]]
    (js/alert (str "ooooops... não deu pra procurar por " query))
    db))














;; views

(defn results-table [results]
  [:div {:style {:overflow-x :auto}}
   [:table.table-striped
    [:thead
     [:tr
      [:td {:style {:width "50%"}}
       [:strong "URL"]]
      [:td {:style {:width "30%"}}
       [:strong "Updated"]]
      [:td {:style {:width "20%"}}
       [:strong "Language"]]]]
    [:tbody
     (for [result results]
       ^{:key (hash (:url result))}
       [:tr
        [:td (:url result)]
        [:td [:code (:updated-at result)]]
        [:td (:language result)]])]]])


(defn general-results []
  (let [results* (re-frame/subscribe [:github/results])]
    (fn general-results-render []
      [:section
       [:h3 "All Results (" (count @results*) ")"]
       [results-table @results*]])))


(defn clj-results []
  (let [results* (re-frame/subscribe [:github/clj-results])]
    (fn general-results-render []
      [:section
       [:h3 "Clj Results (" (count @results*) ")"]
       [results-table @results*]])))

(defn view []
  (let [loading?* (re-frame/subscribe [:github/loading?])]
    (fn view-render []
      [re-com/v-box
       :gap "25px"
       :children [[re-com/input-text
                   :change-on-blur? true
                   :model (reagent/atom "")
                   :disabled? @loading?*
                   :on-change #(re-frame/dispatch [:github/search %])
                   :placeholder "search for something on GitHub..."]
                  [re-com/h-split
                   :panel-1 [general-results]
                   :panel-2 [clj-results]]]])))

