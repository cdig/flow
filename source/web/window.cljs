;; Wraps the browser window object and APIs

;; Side Effects √
;; State –
;; Deps —

(ns web.window)

(def window (atom nil)) ;; js/window

;; ACCESSORS

(defn width []
  (.-innerWidth @window))

(defn height []
  (.-innerHeight @window))

;; FUNCTIONS

(defn rAF! [callback]
  (.requestAnimationFrame @window callback))

(defn addEventListener! [type handler]
  (.addEventListener @window type handler))

(defn setup!
  "We need to be given the window from outside, since it might not be available at js/window"
  [win]
  (reset! window win))
