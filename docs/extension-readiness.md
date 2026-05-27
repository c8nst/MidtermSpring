# Extension Readiness

## Best-supported extension: add a new card effect

The design supports a new action card (for example a custom "draw three" or swap-hands effect) most cleanly.

### Where to implement it

1. **`Card`** — add rank detection if the card uses a new suffix or code.
2. **`PlayRules`** — only if the new card has special matching rules (most effects match by color or rank like existing action cards).
3. **`TurnEffects.applyAfterPlay`** — add a branch for the new effect; this is the single place turn-advance and extra draws are applied.
4. **`CharacterizationTests`** — add tests for legality and effect behavior before changing production code.

### Why this is plausible

- Legality is centralized in `PlayRules`.
- Post-play effects are isolated in `TurnEffects`.
- The CLI (`ConsoleUI`) and turn loop (`GameEngine`) do not need to know effect details beyond calling `TurnEffects`.

## What still makes change difficult

- **Bot strategy** (`BotStrategy`) hard-codes priority order (draw two, skip, number, wild). A new powerful card would need an explicit bot preference rule.
- **Deck building** lives in `GameState.buildDeck()` as string literals; adding cards requires editing that method and tests.
- **String card codes** — no enum or type system prevents invalid codes at compile time.
- **Static `Random` in `GameContext`** — replay logs or deterministic simulations would need dependency injection into `GameState.draw` and shuffle paths.

## Other extensions (secondary)

| Extension | Entry point | Difficulty |
|-----------|-------------|------------|
| Smarter bot | Replace or extend `BotStrategy` | Low |
| Rule variant (e.g. must-play) | `GameEngine.resolveDrawChoice` / `ConsoleUI.askHumanCardChoice` | Medium |
| Replay log | Wrap `GameEngine.playOneTurn` or inject a listener | Medium |
| Replace CLI view | New class implementing the same prompts as `ConsoleUI` | Medium–high |
