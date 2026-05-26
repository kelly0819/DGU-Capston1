"""
Supabase 마이그레이션 SQL을 순서대로 출력하는 헬퍼.

Supabase는 supabase-py에서 임의 DDL을 실행할 수 없으므로,
실제 적용은 Supabase Dashboard SQL Editor에 붙여넣거나 supabase CLI를 사용해야 한다.
본 스크립트는 마이그레이션 파일을 정렬·출력해 적용 순서를 명확히 보여준다.

사용법:
    python AI/scripts/apply_migrations.py            # 모든 SQL 출력, python 안 되면 python3
    python AI/scripts/apply_migrations.py --list     # 파일명만 나열
"""
import argparse
from pathlib import Path

MIGRATIONS_DIR = Path(__file__).parent.parent / "db" / "migrations"


def list_migrations() -> list[Path]:
    return sorted(MIGRATIONS_DIR.glob("*.sql"))


def print_all() -> None:
    files = list_migrations()
    if not files:
        print(f"마이그레이션 파일이 없습니다: {MIGRATIONS_DIR}")
        return

    print(f"=== {len(files)}개의 마이그레이션 ===\n")
    for f in files:
        print(f"-- {'=' * 60}")
        print(f"-- File: {f.name}")
        print(f"-- {'=' * 60}")
        print(f.read_text(encoding="utf-8"))
        print()

    print("\n위 SQL을 Supabase Dashboard SQL Editor에 순서대로 붙여넣어 실행하세요.")


def print_list() -> None:
    files = list_migrations()
    print(f"적용 순서 ({len(files)}개):")
    for f in files:
        print(f"  - {f.name}")


def main() -> None:
    parser = argparse.ArgumentParser(description="Supabase 마이그레이션 출력 헬퍼")
    parser.add_argument("--list", action="store_true", help="파일명만 나열")
    args = parser.parse_args()

    if args.list:
        print_list()
    else:
        print_all()


if __name__ == "__main__":
    main()