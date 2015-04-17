;; Wraps the browser mouse APIs

;; Side Effects X
;; State X
;; Deps —

(ns browser.mouse)

;; NICENESS

(defn event->pos [event]
  {:x (.-clientX event)
   :y (.-clientY event)})
