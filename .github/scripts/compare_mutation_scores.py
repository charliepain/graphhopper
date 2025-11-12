#!/usr/bin/env python3

import sys


def main():
    if len(sys.argv) < 3:
        print("Usage: python compare_mutation_scores.py <previous> <current>", file=sys.stderr)
        sys.exit(1)

    try:
        previous = float(sys.argv[1])
        current = float(sys.argv[2])
    except ValueError:
        print("Scores must be numbers.", file=sys.stderr)
        sys.exit(1)

    if current + 0.000001 < previous:
        print(
            f"Mutation score went down: previous={previous:.2f}% current={current:.2f}%",
            file=sys.stderr,
        )
        sys.exit(1)

    print(
        f"Mutation score OK: previous={previous:.2f}% current={current:.2f}%"
    )


if __name__ == "__main__":
    main()

