;; Wraps the browser document object and APIs

;; Side Effects √
;; State –
;; Deps —

(ns browser.document)

(def document js/document)
(def body (.-body document))

;; FUNCTIONS

(defn createElement! [name]
  (.createElement document "canvas"))

;; NICENESS

(defn appendChild! [element]
  (.appendChild body element)
  element)
