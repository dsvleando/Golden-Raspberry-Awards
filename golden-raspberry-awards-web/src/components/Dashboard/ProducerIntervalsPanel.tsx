import React from "react";
import {
  Paper,
  Title,
  Table,
  LoadingOverlay,
  Stack,
  Text,
} from "@mantine/core";
import { MaxMinWinIntervalResponse, ProducerInterval } from "../../api";

interface ProducerIntervalsPanelProps {
  intervals: MaxMinWinIntervalResponse;
  isLoading: boolean;
}

const ProducerIntervalsPanel: React.FC<ProducerIntervalsPanelProps> = ({
  intervals,
  isLoading,
}) => (
  <Paper shadow="xs" p="md" withBorder>
    <Title order={4} mb="md" c="#333">
      Produtores com maior e menor intervalo entre vitórias
    </Title>
    <LoadingOverlay visible={isLoading} />
    <Stack gap="md">
      <div>
        <Text fw={600} mb="sm" c="#333">
          Máximo
        </Text>
        <Table striped highlightOnHover>
          <Table.Thead>
            <Table.Tr>
              <Table.Th>Produtor</Table.Th>
              <Table.Th>Intervalo</Table.Th>
              <Table.Th>Ano Anterior</Table.Th>
              <Table.Th>Ano Seguinte</Table.Th>
            </Table.Tr>
          </Table.Thead>
          <Table.Tbody>
            {intervals.max.map((producer: ProducerInterval, index: number) => (
              <Table.Tr key={`max-${index}`}>
                <Table.Td>{producer.producer}</Table.Td>
                <Table.Td>{producer.interval}</Table.Td>
                <Table.Td>{producer.previousWin}</Table.Td>
                <Table.Td>{producer.followingWin}</Table.Td>
              </Table.Tr>
            ))}
          </Table.Tbody>
        </Table>
      </div>
      <div>
        <Text fw={600} mb="sm" c="#333">
          Mínimo
        </Text>
        <Table striped highlightOnHover>
          <Table.Thead>
            <Table.Tr>
              <Table.Th>Produtor</Table.Th>
              <Table.Th>Intervalo</Table.Th>
              <Table.Th>Ano Anterior</Table.Th>
              <Table.Th>Ano Seguinte</Table.Th>
            </Table.Tr>
          </Table.Thead>
          <Table.Tbody>
            {intervals.min.map((producer: ProducerInterval, index: number) => (
              <Table.Tr key={`min-${index}`}>
                <Table.Td>{producer.producer}</Table.Td>
                <Table.Td>{producer.interval}</Table.Td>
                <Table.Td>{producer.previousWin}</Table.Td>
                <Table.Td>{producer.followingWin}</Table.Td>
              </Table.Tr>
            ))}
          </Table.Tbody>
        </Table>
      </div>
    </Stack>
  </Paper>
);

export default ProducerIntervalsPanel;
