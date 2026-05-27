# Refactoring Report

## What behavior was characterized before refactoring?

Before moving code out of `Main`, characterization tests were added (and the original `selfTest` checks were preserved) for:

- Card parsing: color, rank, number, and point values
- Legal play by color, number, and action type (skip, reverse, draw two)
- Wild and wild draw four always being playable, plus called-color matching after a wild
- Bot card and color selection priorities
- Win scoring from opponents' remaining hands
- Deck draw with discard-pile recycling and the `W` fallback when both piles are empty
- Two-player reverse effectively skipping the opponent (double advance)
- Edge cases: out-of-range index as an invalid move, and humans being allowed to keep a drawn card when they decline to play it

Tests describe this implementation's quirks, not official UNO rules.

## Worst design problems found

1. **One large `Main` class** with global mutable static fields for all game state.
2. **Duplicated legal-play logic** in `isLegal`, bot selection, and the human play path.
3. **Console I/O mixed with rules** — printing, prompts, and penalties lived beside turn logic.
4. **Primitive string cards** — color/rank/number parsing scattered as string checks.
5. **Action effects embedded in the turn loop** — skip, reverse, draw two, and wild draw four were long `if` chains.
6. **Bot strategy coupled to globals** — bot methods read `upCard` and `calledColor` from static state.

## Refactorings performed

| Step | Change |
|------|--------|
| 1 | Added `test/CharacterizationTests.java` (28 checks) run via `scripts/test.sh` / `Main --self-test` |
| 2 | Extracted `Card` for parsing and scoring |
| 3 | Extracted `PlayRules` to centralize `isLegal` |
| 4 | Extracted `BotStrategy` and removed four duplicated legality loops |
| 5 | Extracted `GameState` for players, deck, discard, scores, and draw recycling |
| 6 | Extracted `TurnEffects` for post-play action handling |
| 7 | Extracted `ConsoleUI` for prompts and table output |
| 8 | Extracted `GameEngine` for turn orchestration without direct `Scanner` use |
| 9 | Slimmed `Main` to CLI argument parsing and wiring |

## Behavior intentionally preserved

- Card codes and matching rules from `docs/rules.html`
- Humans may type `draw` even when holding a legal card
- Illegal card **code** re-prompts; illegal **index** draws a penalty card and ends the turn
- Bots auto-play a legal drawn card; humans choose with `y/n`
- Two-player reverse skips the opponent
- All hands visible in the terminal
- UNO message at one card left
- 3000-turn safety limit
- Empty-deck recycling and `W` fallback

## Risks that remain

- `GameContext` still holds a static `Random`, which makes parallel tests harder.
- `GameEngine` and `TurnEffects` are not fully covered by automated multi-turn simulations.
- `ConsoleUI` is still constructed with `System.in` even in quiet bot mode.
- Card representation remains string-based; typos in codes are still possible at compile time.
