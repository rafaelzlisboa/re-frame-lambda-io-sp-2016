(ns lambda-io.core
  (:require
    [lambda-io.main]
    [re-frisk.core :refer [enable-re-frisk!]]
    [re-frame.core :as re-frame]
    [reagent.core :as reagent]))

(enable-console-print!)

(defn on-js-reload [] reagent/force-update-all)

(defn ^:export init []
  (re-frame/dispatch-sync [:main/init])
  (enable-re-frisk!)
  (reagent/render [lambda-io.main/view] (.getElementById js/document "app")))
