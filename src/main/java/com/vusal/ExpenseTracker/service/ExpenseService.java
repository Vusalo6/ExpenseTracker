package com.vusal.ExpenseTracker.service;
import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.Entity.Expense;
import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.dto.ExpenseResponseDtoList;
import com.vusal.ExpenseTracker.dto.ExpenseRequestDto;
import com.vusal.ExpenseTracker.dto.ExpenseResponseDto;
import com.vusal.ExpenseTracker.dto.ExpenseUpdateDto;
import com.vusal.ExpenseTracker.enums.Period;
import com.vusal.ExpenseTracker.exception.ResourceNotFoundException;
import com.vusal.ExpenseTracker.repos.CategoryRepo;
import com.vusal.ExpenseTracker.repos.ExpenseRepo;
import com.vusal.ExpenseTracker.repos.UserRepo;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
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
    public ExpenseResponseDtoList getExpensesForAuthenticatedUser(String email) {

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

        return new ExpenseResponseDtoList(responseDtos, total);
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
    public ExpenseResponseDtoList findByUserAndCategory(String email, Long categoryId){

        User user = userRepo.findUserByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User doesnt exist"));

        Category category = findCategoryById(categoryId);


        List<ExpenseResponseDto> expenseResponseDtos =new ArrayList<>();

        List<Expense> expenses =
                expenseRepo.findByUserAndCategory(user, category);
        BigDecimal total =BigDecimal.ZERO;

        for (Expense e:expenses){
            ExpenseResponseDto expenseResponseDto = new ExpenseResponseDto();
            expenseResponseDto.setId(e.getId());
            expenseResponseDto.setCategoryName(e.getCategory().getName());
            expenseResponseDto.setDate(e.getDate());
            expenseResponseDto.setAmount(e.getAmount());
            expenseResponseDto.setDescription(e.getDescription());
            total=total.add(e.getAmount());
            expenseResponseDtos.add(expenseResponseDto);


        }
        return  new ExpenseResponseDtoList(expenseResponseDtos,total);

    }
    public ExpenseResponseDtoList findByUserAndDate(String email, LocalDate date){
        User user =userRepo
                .findUserByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User doesnt exist"));
        List<Expense> expenseList= expenseRepo.findByUserAndDate(user, date);
        List<ExpenseResponseDto> listOfExpenseResponseDtos = new ArrayList<>();
        BigDecimal total =BigDecimal.ZERO;
        for(Expense e: expenseList){
            ExpenseResponseDto expenseResponseDto = new ExpenseResponseDto();
            expenseResponseDto.setDescription(e.getDescription());
            expenseResponseDto.setDate(e.getDate());
            expenseResponseDto.setId(e.getId());
            expenseResponseDto.setCategoryName(e.getCategory().getName());
            expenseResponseDto.setAmount(e.getAmount());

            listOfExpenseResponseDtos.add(expenseResponseDto);
            total=total.add(e.getAmount());
        }
        return new ExpenseResponseDtoList(listOfExpenseResponseDtos,total);
    }
    public ExpenseResponseDtoList getByUserAndPeriod(String email, Period period){
        User user =userRepo
                .findUserByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User doesnt exists"));


        LocalDate today =LocalDate.now();
        LocalDate startDate;
        LocalDate endDate;
        List<Expense> expenses =new ArrayList<>();
        List<ExpenseResponseDto> responseDtoList=new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        switch (period){
            case THIS_WEEK -> {
                startDate= today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                endDate =today;
                expenses =expenseRepo.findByUserAndDateBetween(user, startDate, endDate);
            }
            case LAST_WEEK -> {
                LocalDate thisWeekStart =today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                startDate =thisWeekStart.minusWeeks(1);
                endDate=thisWeekStart.minusDays(1);
                expenses =expenseRepo.findByUserAndDateBetween(user, startDate, endDate);
            }
            case THIS_MONTH -> {
                startDate = today.withDayOfMonth(1);
                endDate = today;
                expenses =expenseRepo.findByUserAndDateBetween(user, startDate, endDate);
            }

            case LAST_MONTH -> {
                LocalDate lastMonth = today.minusMonths(1);

                startDate = lastMonth.withDayOfMonth(1);
                endDate = lastMonth.with(
                        TemporalAdjusters.lastDayOfMonth());
                expenses =expenseRepo.findByUserAndDateBetween(user, startDate, endDate);
            }

        }
        for(Expense e:expenses){
            ExpenseResponseDto responseDto =new ExpenseResponseDto();
            responseDto.setAmount(e.getAmount());
            responseDto.setDate(e.getDate());
            responseDto.setCategoryName(e.getCategory().getName());
            responseDto.setId(e.getId());
            responseDto.setDescription(e.getDescription());
            total=total.add(e.getAmount());
            responseDtoList.add(responseDto);

        }

       return new ExpenseResponseDtoList(responseDtoList,total);

    }

    public ExpenseResponseDtoList findByUserAndCategoryAndDate(String email, Long categoryId, LocalDate date){
        User user =userRepo.findUserByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
        Category category=findCategoryById(categoryId);
        List<Expense> expenseList = expenseRepo.findByUserAndCategoryAndDate(user,category,date);
        List<ExpenseResponseDto> listOfExpenseResponseDtos= new ArrayList<>();
        BigDecimal total= BigDecimal.ZERO;
        for (Expense e:expenseList){
            ExpenseResponseDto expenseResponseDto =new ExpenseResponseDto();
            expenseResponseDto.setAmount(e.getAmount());
            expenseResponseDto.setDate(e.getDate());
            expenseResponseDto.setDescription(e.getDescription());
            expenseResponseDto.setId(e.getId());
            expenseResponseDto.setCategoryName(e.getCategory().getName());
            total= total.add(e.getAmount());
            listOfExpenseResponseDtos.add(expenseResponseDto);

        }
        return new ExpenseResponseDtoList(listOfExpenseResponseDtos,total);
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
