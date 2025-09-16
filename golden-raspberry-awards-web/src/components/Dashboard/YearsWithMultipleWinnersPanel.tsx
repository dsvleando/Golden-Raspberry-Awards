import React from "react";
import { Paper, Title, Table, LoadingOverlay } from "@mantine/core";
import { YearWithMultipleWinners } from "../../api";

interface YearsWithMultipleWinnersPanelProps {
  years: YearWithMultipleWinners[];
  isLoading: boolean;
}

const YearsWithMultipleWinnersPanel: React.FC<
  YearsWithMultipleWinnersPanelProps
> = ({ years, isLoading }) => (
  <Paper shadow="xs" p="md" withBorder>
    <Title order={4} mb="md" c="#333">
      List years with multiple winners
    </Title>
    <LoadingOverlay visible={isLoading} />
    <Table striped highlightOnHover>
      <Table.Thead>
        <Table.Tr>
          <Table.Th>Year</Table.Th>
          <Table.Th>Win Count</Table.Th>
        </Table.Tr>
      </Table.Thead>
      <Table.Tbody>
        {years.map((year) => (
          <Table.Tr key={year.year}>
            <Table.Td>{year.year}</Table.Td>
            <Table.Td>{year.winnerCount}</Table.Td>
          </Table.Tr>
        ))}
      </Table.Tbody>
    </Table>
  </Paper>
);

export default YearsWithMultipleWinnersPanel;
