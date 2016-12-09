(ns lambda-io.main
  (:require
    [lambda-io.gifs :as gifs]
    [lambda-io.github :as github]
    [lambda-io.slides :as slides]
    [re-com.core :as re-com]
    [re-frame.core :as re-frame]))

(def tabs [{:id :slides :label "Slides"}
           {:id :gifs :label "GIFs"}
           {:id :github :label "GitHub Search"}])




;; events

(re-frame/reg-event-db :main/select-tab (fn [db [_event-name tab]]
                                          (assoc db :selected-tab tab)))
(re-frame/reg-event-db :main/init (fn [_db] {:github lambda-io.github/initial-db
                                             :slides lambda-io.slides/initial-db
                                             :selected-tab :slides}))





















;; subscriptions
(re-frame/reg-sub :main/selected-tab (fn [db] (:selected-tab db)))











;; views

(defn view-selector [current-tab]
  [re-com/horizontal-tabs
   :tabs tabs
   :model current-tab
   :on-change #(re-frame/dispatch [:main/select-tab %])])

(defn view []
  (let [current-tab* (re-frame/subscribe [:main/selected-tab])]
    (fn main-render []
      [:main {:style {:margin 50}}
       [:h5 "(<3 clojurescript re-frame)"]
       [view-selector @current-tab*]
       (condp = @current-tab*
         :gifs [gifs/view]
         :github [github/view]
         :slides [slides/view])])))
