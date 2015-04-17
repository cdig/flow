(ns app.behaviour
  (:require [app.db :as db]))

;; Public
;; Add a new behaviour to the given element. Returns the bid of the new behaviour.
;; ? Reload-safe
(defn add! [eid bname custom-state]
  (assert (keyword? eid))
  (assert (keyword? bname))
  (assert (map? custom-state))
  
  (let [bid (db/next-id! :bid)
        default-behaviour (:default-behaviour @db/cache)
        template-state (get-in @db/cache [:behaviour-templates bname :state])
        behaviour (merge default-behaviour
                               {:bid bid
                                :bname bname
                                :state (merge (:state default-behaviour)
                                              template-state
                                              custom-state)})]
    
    ;; It's okay to not have a template, but the behaviour won't do anything.
    ;; If the behaviour is registered later on, it will start working so long as
    ;; this behaviour has all the necessary state! The case of missing state is
    ;; ambiguous behaviour.
    (when-not template-state (print "Warning: Unknown Behaviour " bname))
    
    ;; Save the behaviour instance to the db
    (swap! db/state assoc-in [:behaviours bid] behaviour)
    
    ;; Add the behaviour id to the entity
    (swap! db/state update-in [:entities eid :bids] conj bid)
    
    ;; Cache that this behaviour belongs to the given entity
    (swap! db/cache assoc-in [:bid->eid bid] eid)
    
    ;; Prep all systems used by this bname
    (doseq [sname (get-in @db/cache [:bname->snames bname])]
      
      ;; Cache that this behaviour instance needs to be updated by specific systems
      (swap! db/cache update-in [:sname->bids sname] #(into #{bid} %1))
      
      ;; Fire the :create message for the system
      ;; TODO: It's probably a good idea to queue these and run them later so we don't go too deep
      (let [callback (get-in @db/cache [:message->sname->callback :create sname])]
        (when callback
          (let [entity (get-in @db/state [:entities eid])
                state (:state behaviour)
                result (callback entity state)]
            (when result
              (swap! db/state assoc-in [:behaviours bid :state] result))))))
    
    ;; Return the new bid
    bid))

    


;; Public
;; Register a new behaviour template that can be used to create behaviours
;; √ Reload-safe
(defn register-template! [{:keys [bname] :as template-definition}]
  (let [template-state (:state template-definition)
        systems (:systems template-definition)]
    
    ;; Cache the template
    (swap! db/cache assoc-in [:behaviour-templates bname] {:state template-state})
    
    ;; Update state for existing instances of this behaviour
    (doseq [[bid behaviour] (:behaviours @db/state)]
      (when (= bname (:bname behaviour))
        (doseq [[template-k template-v] template-state]
          (when-not (get-in behaviour [:state template-k])
            (swap! db/state assoc-in [:behaviours bid :state template-k] template-v)))))
    
    ;; Set up system-related stuff for this behaviour
    (doseq [sname systems]
      ;; Cache that this bname uses the given system
      (swap! db/cache update-in [:bname->snames bname] #(into #{sname} %1))
      
      ;; Update cache for existing instances of this behaviour
      (doseq [[bid behaviour] (:behaviours @db/state)]
        (when (= bname (:bname behaviour))
          (swap! db/cache update-in [:sname->bids sname] #(into #{bid} %1)))))))
