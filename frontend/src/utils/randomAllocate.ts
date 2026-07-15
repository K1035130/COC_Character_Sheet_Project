export function randomAllocate(total: number, count: number, min: number, max: number): number[] {
  if (total < min * count) {
    throw new Error(`Total must be at least ${min * count}`);
  }
  if (total > max * count) {
    throw new Error(`Total must be at most ${max * count}`);
  }

  const values = new Array(count).fill(min);
  let remaining = total - min * count;

  while (remaining > 0) {
    const openIndexes = values.reduce<number[]>((acc, value, index) => {
      if (value < max) {
        acc.push(index);
      }
      return acc;
    }, []);
    const index = openIndexes[Math.floor(Math.random() * openIndexes.length)];
    const capacity = max - values[index];
    const amount = Math.min(capacity, remaining, 1 + Math.floor(Math.random() * capacity));
    values[index] += amount;
    remaining -= amount;
  }

  for (let i = values.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [values[i], values[j]] = [values[j], values[i]];
  }

  return values;
}
