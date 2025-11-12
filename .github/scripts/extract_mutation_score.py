#!/usr/bin/env python3

import os
import re
import sys


def main():
    if len(sys.argv) < 2:
        print("Usage: python extract_mutation_score.py <report_root>", file=sys.stderr)
        sys.exit(1)

    report_root = sys.argv[1]
    if not os.path.isdir(report_root):
        print(f"Report folder not found: {report_root}", file=sys.stderr)
        sys.exit(1)

    sub_dirs = sorted(
        [name for name in os.listdir(report_root) if os.path.isdir(os.path.join(report_root, name))]
    )
    if not sub_dirs:
        print("No PIT reports were generated.", file=sys.stderr)
        sys.exit(1)

    index_path = os.path.join(report_root, sub_dirs[-1], "index.html")
    if not os.path.exists(index_path):
        print(f"Cannot find index.html in {index_path}", file=sys.stderr)
        sys.exit(1)

    with open(index_path, encoding="utf-8") as handle:
        html = handle.read()

    match = re.search(r"Mutation Coverage.*?>(\d+(?:\.\d+)?)%", html, re.DOTALL)
    if not match:
        print("Could not read the mutation score.", file=sys.stderr)
        sys.exit(1)

    print(match.group(1))


if __name__ == "__main__":
    main()
