#!/bin/bash

# API Test Framework Runner Script
# Usage: ./run-tests.sh [options]

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Default values
THREAD_COUNT=1
TEST_GROUP=""
ENVIRONMENT="local"
GENERATE_REPORT=false

# Function to print colored output
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Function to show usage
show_usage() {
    cat << EOF
Usage: ./run-tests.sh [OPTIONS]

Options:
    -t, --threads <number>      Number of parallel threads (default: 1)
    -g, --group <name>          Test group: users, products, smoke, or empty for all
    -e, --env <environment>     Environment: local, ci (default: local)
    -r, --report                Generate and open Allure report after tests
    -h, --help                  Show this help message

Examples:
    ./run-tests.sh                              # Run all tests with 1 thread
    ./run-tests.sh -t 4                         # Run all tests with 4 threads
    ./run-tests.sh -g users -t 2                # Run users tests with 2 threads
    ./run-tests.sh -g products -t 2 -r          # Run products tests and generate report
    ./run-tests.sh --threads 4 --report         # Run all tests with 4 threads and report

EOF
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -t|--threads)
            THREAD_COUNT="$2"
            shift 2
            ;;
        -g|--group)
            TEST_GROUP="$2"
            shift 2
            ;;
        -e|--env)
            ENVIRONMENT="$2"
            shift 2
            ;;
        -r|--report)
            GENERATE_REPORT=true
            shift
            ;;
        -h|--help)
            show_usage
            exit 0
            ;;
        *)
            print_error "Unknown option: $1"
            show_usage
            exit 1
            ;;
    esac
done

# Validate thread count
if ! [[ "$THREAD_COUNT" =~ ^[0-9]+$ ]] || [ "$THREAD_COUNT" -lt 1 ]; then
    print_error "Thread count must be a positive integer"
    exit 1
fi

# Build Maven command
MVN_CMD="mvn clean test"

# Add environment
MVN_CMD="$MVN_CMD -Denv=$ENVIRONMENT"

# Add thread count
MVN_CMD="$MVN_CMD -Dthread.count=$THREAD_COUNT"

# Add test group if specified
if [ -n "$TEST_GROUP" ]; then
    case $TEST_GROUP in
        users|products|smoke)
            MVN_CMD="$MVN_CMD -P$TEST_GROUP"
            print_info "Running $TEST_GROUP tests"
            ;;
        *)
            print_error "Invalid test group: $TEST_GROUP"
            print_warning "Valid groups: users, products, smoke"
            exit 1
            ;;
    esac
else
    print_info "Running all tests"
fi

print_info "Environment: $ENVIRONMENT"
print_info "Thread count: $THREAD_COUNT"
print_info "Command: $MVN_CMD"
echo ""

# Run tests
if eval $MVN_CMD; then
    print_info "Tests completed successfully!"
    
    # Generate report if requested
    if [ "$GENERATE_REPORT" = true ]; then
        print_info "Generating Allure report..."
        mvn allure:serve
    fi
else
    print_error "Tests failed!"
    exit 1
fi
