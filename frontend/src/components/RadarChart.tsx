import {
  PolarAngleAxis,
  PolarGrid,
  PolarRadiusAxis,
  Radar,
  RadarChart as RechartsRadarChart,
  ResponsiveContainer,
} from 'recharts';
import { CORE_ATTRIBUTES } from '../types/character';
import type { AttributeKey } from '../types/character';

interface CoreAttributeRadarChartProps {
  attributes: Record<AttributeKey, number>;
}

function toChartData(keys: AttributeKey[], attributes: Record<AttributeKey, number>) {
  return keys.map((key) => ({ attribute: key, value: attributes[key] ?? 0 }));
}

export function CoreAttributeRadarChart({ attributes }: CoreAttributeRadarChartProps) {
  const coreData = toChartData(CORE_ATTRIBUTES, attributes);

  return (
    <div className="radar-chart">
      <h3>Core Attributes</h3>
      <ResponsiveContainer width="100%" height={280}>
        <RechartsRadarChart data={coreData}>
          <PolarGrid />
          <PolarAngleAxis dataKey="attribute" />
          <PolarRadiusAxis angle={30} domain={[0, (max: number) => Math.max(100, Math.ceil(max * 1.05))]} />
          <Radar name="Core" dataKey="value" stroke="#5b8def" fill="#5b8def" fillOpacity={0.4} />
        </RechartsRadarChart>
      </ResponsiveContainer>
    </div>
  );
}
