# re-frame form example with spec

Now, when we want to create a form using re-frame, we need to spend a lot of time designing the form due to lack of samples.

This repository shows the example of the form design with clojure's spec for validations.

The base architecture is based on the re-frame template.
But I introduce two mandatory components (files).

## domain

_domain_ means data's spec. So, if you wanna implement the email form, you should write the email's acceptable regular expression into this component.

example is in the `src/cljs/form_validation_example/domain.cljs`

```clojure
(s/def ::email
  (s/and string? (partial re-matches #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")))
```

I reccomend you to write this domain's code using clojure's spec !

## valid

_valids_ means the validation of each forms to show users error message using the natual languages.

_valid_ component depends on _domain_ component.

We want to separate the form's validation and the view because of to avoid the complexity of the view (some frameworks like react often cause this problem.).

So that, I propose we should implement error validations in this component.
