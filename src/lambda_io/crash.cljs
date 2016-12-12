(ns lambda-io.crash)

;;; basic clojure(script)

;; set
(def animals #{:cat :dog :peixe-boi})
animals

;; vector
(def numbers-i-hate [1 2 3 4 5 6 7 8 9])
numbers-i-hate

;; map
(def book {:title "Admirável Mundo Novo"
           :author "Aldous Huxley"
           :isbn 9788525056009
           :edition 22})
book

;; destructuring a map
(let [{:keys [title author]} book]
  ;; title =>  "Admirável Mundo Novo"
  ;; author => "Aldous Huxley"
  (str (first title) (first author)))

(str (first (:title book))
     (first (:author book)))






































;; defining a function
(defn hello [name]
  (str "hello, " name))

;; using it
(println (hello "lambda-io!"))














































;; atoms
(def i-can-change* (atom 1))

;; inc(rease) the value in the atom
(swap! i-can-change* inc)

;; access its value
@i-can-change*      ; => 2
i-can-change*

