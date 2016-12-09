(ns lambda-io.gifs)

(def gifs ["avatar.gif" "eye.gif" "godzilla.gif" "missiles.gif" "shinji.gif" "ship.gif" "tunnel.gif"])

(defn view []
  [:img {:src (str "img/" (rand-nth gifs))}])