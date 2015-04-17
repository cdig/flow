;; Wraps the browser window object and APIs

;; Side Effects √
;; State –
;; Deps —

(ns browser.window)

(def window js/window)

;; ACCESSORS

(defn width []
  (.-innerWidth window))

(defn height []
  (.-innerHeight window))

;; FUNCTIONS

(defn rAF! [callback]
  (.requestAnimationFrame window callback))
