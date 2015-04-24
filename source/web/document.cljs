;; Wraps the browser document object and APIs

;; Side Effects √
;; State –
;; Deps —

(ns web.document)

(def document (atom nil)) ;; js/document
(def body (atom nil)) ;; (.-body document)

;; FUNCTIONS

(defn createElement! [name]
  (.createElement @document "canvas"))

;; NICENESS

(defn appendChild! [element]
  (.appendChild @body element)
  element)

(defn setup! [doc]
  (reset! document doc)
  (reset! body (.-body doc)))
