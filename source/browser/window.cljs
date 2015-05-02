;; Wraps the browser window object and APIs

;; Side Effects √
;; State –
;; Deps —

(ns browser.window)

(def window (atom nil)) ;; js/window

;; ACCESSORS

(defn width []
  (.-innerWidth @window))

(defn height []
  (.-innerHeight @window))

(defn current-size
  [& _]
  {:w (width)
   :h (height)})

;; FUNCTIONS

(defn rAF! [callback]
  (.requestAnimationFrame @window callback))

(defn addEventListener! [type handler]
  (.addEventListener @window type handler))

(defn setup!
  "We need to be given the window from outside, since it might not be available at js/window (eg: node)"
  [win]
  (reset! window win))
