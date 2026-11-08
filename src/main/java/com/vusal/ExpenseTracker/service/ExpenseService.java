package com.vusal.ExpenseTracker.service;
import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.Entity.Expense;
import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.dto.ExpenseListResponseDto;
import com.vusal.ExpenseTracker.dto.ExpenseRequestDto;
import com.vusal.ExpenseTracker.dto.ExpenseResponseDto;
import com.vusal.ExpenseTracker.dto.ExpenseUpdateDto;
import com.vusal.ExpenseTracker.exception.ResourceNotFoundException;
import com.vusal.ExpenseTracker.repos.CategoryRepo;
import com.vusal.ExpenseTracker.repos.ExpenseRepo;
import com.vusal.ExpenseTracker.repos.UserRepo;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepo expenseRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;


    public ExpenseService(ExpenseRepo expenseRepo,UserRepo userRepo,CategoryRepo categoryRepo){
        this.expenseRepo=expenseRepo;
        this.userRepo=userRepo;
        this.categoryRepo=categoryRepo;
    }
    public BigDecimal getTotal(List<Expense> expenses){
        BigDecimal total = BigDecimal.ZERO;

        for(Expense e:expenses){
            total =  total.add(e.getAmount());
        }
        return total;
    }
    public ExpenseListResponseDto getExpensesForAuthenticatedUser(String email) {

        User user = userRepo.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + email
                ));

        List<Expense> expenses = expenseRepo.findByUser(user);

        List<ExpenseResponseDto> responseDtos = expenses.stream()
                .map(e -> new ExpenseResponseDto(
                        e.getAmount(),
                        e.getCategory().getName(),
                        e.getDescription(),
                        e.getDate(),
                        e.getId()
                ))
                .toList();

        BigDecimal total = getTotal(expenses);

        return new ExpenseListResponseDto(responseDtos, total);
    }
    public Expense returnExpenseForUser(Long id,String email){
       return expenseRepo.findByIdAndUser_Email(id,email).orElseThrow(()->new ResourceNotFoundException("Expense not found "));

    }



    public void createExpense(ExpenseRequestDto expense,String email){
        User user = userRepo.findUserByEmail(email).orElseThrow(()->new ResourceNotFoundException("The user with this email doesnt exist"));
        Expense newExpense= new Expense();
        newExpense.setUser(
                user
        );
        Category category= findCategoryById(expense.getCategoryId());

        newExpense.setCategory(category);

        newExpense
                .setDescription(expense.getDescription());
        newExpense.
                setDate(expense.getDate());
        newExpense
                .setAmount(expense.getAmount());

        expenseRepo.save(newExpense);
    }
    public void updateExpense(ExpenseUpdateDto expense, Long id,String email){
        Expense existingExpense = expenseRepo
                .findByIdAndUser_Email(id, email)
                .orElseThrow(()->new ResourceNotFoundException("Expense doesnt exist for update"));
        existingExpense
                .setCategory(findCategoryById(expense.getCategoryId()));
        existingExpense
                .setAmount(expense.getAmount());
        existingExpense
                .setDescription(expense.getDescription());
        existingExpense
                .setDate(expense.getDate());
        expenseRepo
                .save(existingExpense);
    }
    public void deleteExpense(Long id,String email){
        Expense expense= expenseRepo.
                findByIdAndUser_Email(id, email)
                .orElseThrow(()->new ResourceNotFoundException("Resource not found"));
            expenseRepo.delete(expense);
    }
    public List<Expense> findByUser(User user){
        return expenseRepo.findByUser(user);
    }
    public List<Expense> findByUserAndCategory(User user, Category category){
        return expenseRepo.findByUserAndCategory(user,category);

    }
    public List<Expense> findByUserAndDate(User user, LocalDate date){
      return expenseRepo.findByUserAndDate(user, date);
    }
    public List<Expense> getByUserAndDatesBetween(User user,LocalDate fromDate,LocalDate toDate){
      long days = ChronoUnit.DAYS.between(fromDate,toDate);
      if (days<0){
          throw new IllegalArgumentException("from Date cannot be after toDate");
      }
      if (days>90) {
          throw new IllegalArgumentException("Date range cannot exceed 90 days");
      }
       return  expenseRepo.findByUserAndDateBetween(user, fromDate, toDate);

    }
    public List<Expense> getThisWeekExpenses(User user){
        LocalDate today =LocalDate.now();
        LocalDate monday= today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

      return   expenseRepo.findByUserAndDateBetween(user,monday,sunday);

    }
    public List<Expense> getLastWeekExpenses(User user){
        LocalDate today=LocalDate.now();
        LocalDate mondayOfLastWeek = today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        LocalDate sundayOfLastWeek  = mondayOfLastWeek.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        return expenseRepo.findByUserAndDateBetween(user,mondayOfLastWeek,sundayOfLastWeek);
    }

    public List<Expense> getThisMonthExpenses(User user){
        LocalDate today =LocalDate.now();
        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        return expenseRepo.findByUserAndDateBetween(user,firstDayOfMonth,lastDayOfMonth);
    }
    public List<Expense> getLastMonthExpenses(User user){ // how de we get user
        LocalDate today = LocalDate.now();
        LocalDate previousMonth = today.minusMonths(1);
        LocalDate firstDayOfLastMonth = previousMonth.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfLastMonth = previousMonth.with(TemporalAdjusters.lastDayOfMonth());
        return expenseRepo.findByUserAndDateBetween(user,firstDayOfLastMonth,lastDayOfLastMonth);
    }
    public List<Expense> findByUserAndCategoryAndDate(User user, Category category, LocalDate time){
        return expenseRepo.findByUserAndCategoryAndDate(user,category,time);
    }
    public Category findCategoryById(Long id){
        if (id == null) {
            throw new  IllegalArgumentException("Category ID cannot be null");
        }
       return categoryRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Category Doesnt exist") );
    }
    public User findUserById(Long id){
        return  userRepo.findById(id).orElseThrow(()->new  ResourceNotFoundException("Expense not found: " + id));
    }
}
